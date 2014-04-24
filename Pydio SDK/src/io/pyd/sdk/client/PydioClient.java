
package io.pyd.sdk.client;

import io.pyd.sdk.client.auth.CredentialsProvider;
import io.pyd.sdk.client.http.CountingMultipartRequestEntity;
import io.pyd.sdk.client.model.FileNode;
import io.pyd.sdk.client.model.Message;
import io.pyd.sdk.client.model.Node;
import io.pyd.sdk.client.model.NodeFactory;
import io.pyd.sdk.client.model.RepositoryNode;
import io.pyd.sdk.client.transport.Transport;
import io.pyd.sdk.client.transport.TransportFactory;
import io.pyd.sdk.client.utils.Pydio;
import io.pyd.sdk.client.utils.StateHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.util.EncodingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 
 * @author pydio
 *
 */
public class PydioClient {
	
	Transport transport;
	

	
	/**
	 * Create a pydio action by specifying the transport mode and a CredentialsProvider instance
	 * @param transportMode Transport constant
	 * @param p
	 */
	public PydioClient(int transportMode, CredentialsProvider p){
		transport = TransportFactory.getInstance(transportMode, p);
	}	
	
	/**
	 * @param params
	 * @param nodes
	 */
	private void fillParams(Map<String, String> params, Node[] nodes){
		if(nodes != null){
			
			if(nodes.length == 1){
				params.put(Pydio.PARAM_FILE, ((FileNode)(nodes[0])).path());
				return;
			}
			
			for(int i = 0; i < nodes.length; i++){
				String path = ((FileNode)nodes[i]).path();
				params.put(Pydio.PARAM_FILE+"_"+i, path);
			}
		}
	}
	
	public ArrayList<RepositoryNode> listRepository(){		
		String action = Pydio.ACTION_LIST_REPOSITORIES;
		Document doc = transport.getXmlContent(action , new HashMap<String, String>());
		NodeList entries;
		entries = doc.getElementsByTagName("repo");
		
		ArrayList<RepositoryNode> nodes = new ArrayList<RepositoryNode>();		
		for(int i = 0; i < entries.getLength(); i++){
			org.w3c.dom.Node xmlNode = entries.item(i);			
			nodes.add((RepositoryNode)NodeFactory.createNode(xmlNode));
		}
		return nodes;
	}
	
	/**
	 * List all the children of a Node
	 * @param node the Node object to list children
	 * @param lsRecurseOptions recursion options
	 * @param lsOrderOptions recursion order
	 * @return An ArrayList of Node
	 */
	ArrayList<Node> listChildren(Node node, Map<String, String> lsRecurseOptions, Map <String, String> lsOrderOptions){
		
		String action;
		Map<String, String> params = new HashMap<String , String>();
		
		if(node.type() == Node.TYPE_SERVER){
			action = Pydio.ACTION_LIST_REPOSITORIES;
		}else{			
			action = Pydio.ACTION_LIST;
			params.put(Pydio.PARAM_OPTIONS, "al");			
			if(lsRecurseOptions != null){
				params.putAll(lsRecurseOptions);
			}
			
			if(lsOrderOptions != null){
				params.putAll(lsOrderOptions);
			}
		}		
		
		Document doc = transport.getXmlContent(action , params);
		NodeList entries;
		if(node.type() != Node.TYPE_SERVER){
			entries = doc.getDocumentElement().getChildNodes();
		}else{
			entries = doc.getElementsByTagName("repo");
		} 
		
		ArrayList<Node> nodes = new ArrayList<Node>();		
		for(int i = 0; i < entries.getLength(); i++){
			org.w3c.dom.Node xmlNode = entries.item(i);			
			nodes.add(NodeFactory.createNode(xmlNode));
		}
		return nodes;
	}
	
		

	/**
	 * Upload a file on the pydio server
	 * @param node the directory to upload the file in
	 * @param source the file to be uploaded
	 * @param progressHandler Listener to handle upload progress
	 * @param autoRename if set to true the file will be automatically rename if exists on the remote server
	 * @param name the name on the remote server
	 * @return a SUCCESS or ERROR Message
	 */
	Message write(Node node, File source, CountingMultipartRequestEntity.ProgressListener progressHandler , boolean autoRename, String name){		
		String action = Pydio.ACTION_UPLOAD;
		Map<String, String> params = new HashMap<String , String>();
		params.put(Pydio.PARAM_NODE, node.path());	
		params.put(Pydio.PARAM_XHR_UPLOADER, "true");
		String tmp_name = null;
		String fname = source.getName();
		
		if(!EncodingUtils.getAsciiString(EncodingUtils.getBytes(source.getName(), "US-ASCII")).equals(source.getName())){
			tmp_name = fname;
			fname = EncodingUtils.getAsciiString(EncodingUtils.getBytes(source.getName(), "US-ASCII")).replace("?", "") + ".tmp_upload";
		}
		try {
			if(name != null){
				params.put(Pydio.PARAM_APPENDTO_URLENCODED_PART, name);
			}else{
				params.put(Pydio.PARAM_URL_ENCODED, java.net.URLEncoder.encode(fname, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		if(autoRename){
			params.put(Pydio.PARAM_AUTO_RENAME, "true");
		}
	
		Message m = Message.create(transport.putContent(action, params, source, fname, progressHandler));	
		if(tmp_name != null){
			rename(node, tmp_name);
		}
		return m;
	}

	
		
	/**
	 * Download content from the remote server.
	 * @param nodes remote nodes to read content
	 * @param outputStream Outputstream on the local target file
	 * @param progressHandler 
	 */
	void read(Node[] nodes, OutputStream outputStream, ProgressHandler progressHandler){
		String action = Pydio.ACTION_DOWNLOAD;
		Map<String, String> params = new HashMap<String , String>();
		fillParams(params, nodes);
		
		InputStream stream = null;
		try {
			stream = transport.getResponse(action, params).getEntity().getContent();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		// if(stream == null) // TODO
		int read = 0, bufsize = Integer.parseInt(StateHolder.getInstance().getLocalConfig(Pydio.LCONFIG_BUFFER_SIZE));
		
		byte[] buffer = new byte[bufsize];	
		int i = 0;
		try {
			for(;;){
				read = stream.read(buffer);
				if(read == -1) break;
				outputStream.write(buffer, 0, read);
				
				if(progressHandler != null){
					progressHandler.onProgress(i*1024 + read);
				}
				i++;
			}
			stream.close();
			outputStream.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/** 
	 * 
	 * Downlaod content ffrom the server
	 * @param nodes Remotes nodes to read content from
	 * @param target local file to put read content in
	 * @param ProgressHandler 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalStateException 
	 */
	void read(Node[] nodes, File target, ProgressHandler ProgressHandler ) throws IllegalStateException, FileNotFoundException, IOException{
		read(nodes, new FileOutputStream(target), null);		
	}

	/**
	 * Remove node on the server
	 * @param nodes
	 * @return
	 */
	Message remove(Node[] nodes){	
		String action = Pydio.ACTION_DELETE;
		Map<String, String> params = new HashMap<String , String>();		
		fillParams(params, nodes);
		Document doc = transport.getXmlContent(action, params);		
		return Message.create(doc);
	}
	
	/**
	 * 
	 * @param node
	 * @param newBaseName
	 * @return
	 */
	Message rename(Node node, String newBaseName){
		String action = Pydio.ACTION_RENAME;
		Map<String, String> params = new HashMap<String , String>();
		params.put(Pydio.PARAM_FILE, node.path());	
		if(newBaseName.contains("/")){
			params.put(Pydio.PARAM_DEST, newBaseName);
		}else{
			params.put(Pydio.PARAM_FILENAME_NEW, newBaseName);
		}
		Document doc = transport.getXmlContent(action, params);
		return Message.create(doc);
	}
	
	/**
	 * 
	 * @param nodes
	 * @param destinationParent
	 * @return
	 */
	Message copy(Node[] nodes, Node destinationParent){
		String action = Pydio.ACTION_COPY;
		Map<String, String> params = new HashMap<String , String>();		
		fillParams(params, nodes);
		params.put(Pydio.PARAM_DEST, ((FileNode)destinationParent).path());
		Document doc = transport.getXmlContent(action, params);		
		return Message.create(doc);
	}
	
	/**
	 * 
	 * @param nodes
	 * @param destinationParent
	 * @return
	 */
	Message move(Node[] nodes, Node destinationParent, boolean force_del){
		String action = Pydio.ACTION_MOVE;
		Map<String, String> params = new HashMap<String , String>();	
		fillParams(params, nodes);
		params.put(Pydio.PARAM_DEST, ((FileNode)destinationParent).path());
		if(force_del){
			params.put(Pydio.PARAM_FORCE_COPY_DELETE, "true");
		}
		Document doc = transport.getXmlContent(action, params);
		return Message.create(doc);
	}
		
	/**
	 * 
	 * @param node
	 * @param dirname
	 * @return
	 */
	Message createFolder(Node node, String dirname){
		String action = Pydio.ACTION_MKDIR;
		
		Map<String, String> params = new HashMap<String , String>();
		params.put(Pydio.PARAM_DIR, node.path());
		params.put(Pydio.PARAM_DIRNAME, dirname);
		
		Document doc = transport.getXmlContent(action, params);
		return Message.create(doc);
	}
	
	/** 
	 * @param path
	 * @return
	 */
	Message createFile(String path){
		String action = Pydio.ACTION_MKFILE;
		Map<String, String> params = new HashMap<String , String>();
		params.put(Pydio.PARAM_NODE, path);
		
		Document doc = transport.getXmlContent(action, params);
		return Message.create(doc);
	}
	
	
	
	/*
	StatResult[] stat( nodes)
	Message lsync( from,  to, copy) 
	Message applyCheck(node);
	Message compress(nodes, archiveName, compressflat)
	Message prepareChunkDownload( node, chunkCount)
	Message downloadChunk(fileID,chunkIndex)
	*/
	
	
	/*
	 *  SERVER CONFIGS METHODS
	 */
	
	
	/**
	 * Load the remote config registry of the current server. 
	 * @param key the name of the remote config
	 * @return a String value of the config
	 */
	public void getRemoteConfigs(){
		Document doc = transport.getXmlContent(Pydio.ACTION_LIST_PLUGINS , new HashMap<String, String>());
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = null;
		org.w3c.dom.Node result = null;			
		try {
			expr = xpath.compile(Pydio.RCONFIG_UPLOAD_SIZE);
			result = (org.w3c.dom.Node)expr.evaluate(doc, XPathConstants.NODE);
			StateHolder.getInstance().getServer().addConfig(Pydio.RCONFIG_UPLOAD_SIZE, result.getFirstChild().getNodeValue().replace("\"", ""));
			//add all config here
		} catch (XPathExpressionException e1) {
			//publish error message
		}		
	}
}
