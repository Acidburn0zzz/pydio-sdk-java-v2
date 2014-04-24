package io.pyd.sdk.client.model;

import java.util.Properties;

import org.json.JSONObject;




public interface Node{
	
	public final static int TYPE_FILE = 1;
	public final static int TYPE_REPOSITORY = 2;
	public final static int TYPE_SERVER = 3;
	public final static int TYPE_VIRTUAL = 4;	
	
	public NodeSpec getNodeSpec();
	public void setNodeSpec(NodeSpec spec);
	
	public int type();
	
	public String label();
	/**
	 * 
	 * @return 
	 */	
	public String path();
	
	/**
	 * initalizes the calling Node using an XML object
	 * @param an XMl represation of a Node
	 */
	public void initFromXml(org.w3c.dom.Node xml);
	/**
	 * initializes the calling Node properties using a json object
	 * @param A json repsrentation of a Node
	 * 
	 */
	public void initFromJson(JSONObject json);
	/**
	 * initializes the calling Node properties using a Property object
	 * @param p Property object containing all Node properties 
	 */
	public void initFromProperties(Properties prop);
	
}
