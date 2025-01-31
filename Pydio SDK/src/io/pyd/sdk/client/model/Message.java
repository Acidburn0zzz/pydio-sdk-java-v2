package io.pyd.sdk.client.model;

import java.util.ArrayList;

import io.pyd.sdk.client.utils.DefaultEventBus;
import io.pyd.sdk.client.utils.Pydio;
import io.pyd.sdk.client.utils.StateHolder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Message{

	
	/**
	 * Message result SUCCESS
	 */
	public final static int SUCCESS = 1;
	
	/**
	 * Message result ERROR
	 */
	public final static int ERROR 	= 2;
		
	

	
	
	private String message;
	private String type;
	
	
	public String getType() {
		return type;
	}

	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public void setType(String type){
		this.type = type;
	}

	
	public String toString(){
		return "result : " + type + ", message : "+message;
	}
	/**
	 * create a message from the given XML Document and fire associated event
	 * @param doc An instance of XML Document
	 * @return An instance of a Message
	 */
	public static Message create(Document doc){
		
		org.w3c.dom.Node xml_message = doc.getElementsByTagName(Pydio.XML_MESSAGE).item(0);
		
		Message message = new Message();
		if(xml_message != null){
			message.setMessage(xml_message.getTextContent());
			message.setType(xml_message.getAttributes().getNamedItem(Pydio.MESSAGE_PROPERTY_TYPE).getNodeValue());
		}
		try{
			NodeList removes = doc.getElementsByTagName(Pydio.NODE_DIFF_REMOVE).item(0).getChildNodes();
			ArrayList<String> pathes = new ArrayList<String>();
			for(int i = 0; i < removes.getLength(); i++){
				pathes.add(removes.item(i).getAttributes().getNamedItem(Pydio.NODE_PROPERTY_FILENAME).getNodeValue());
			}
			StateHolder.getInstance().bus().publish(new TreeRemoveEvent(pathes));
		}catch(Exception e){
			
		}
		
		ArrayList<Node> nodes = null;
		try{
			NodeList adds = doc.getElementsByTagName(Pydio.NODE_DIFF_ADD).item(0).getChildNodes();
			nodes = new ArrayList<Node>();
			for(int i = 0; i < adds.getLength(); i++){
				nodes.add(NodeFactory.createNode(adds.item(i)));				
			}
			StateHolder.getInstance().bus().publish(new TreeAddEvent(nodes));
		}catch(Exception e){
			
		}
		
		try{
			NodeList updates = doc.getElementsByTagName(Pydio.NODE_DIFF_UPDATE).item(0).getChildNodes();
			nodes = new ArrayList<Node>();
			for(int i = 0; i < updates.getLength(); i++){			
				nodes.add(NodeFactory.createNode(updates.item(i)));			
			}
			StateHolder.getInstance().bus().publish(new TreeUpdateEvent(nodes));
		}catch(Exception e){
			
		}
		
		return message;
	}
	
	public static Message create(String type, String message){
		Message m = new Message();
		m.type = type;
		m.message = message;
		return m;
	}
	
}