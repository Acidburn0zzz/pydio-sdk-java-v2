package io.pyd.sdk.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
/**
 * Class that wrap a server properties
 * @author pydio
 *
 */
public class ServerNode implements Node{
		
	private boolean legacy = false;
	private boolean SSLselfSigned = false;
	private String protocol;
	private String host;
	private String path;
	private Map<String, String> remoteCapacities;
	private Map<String, String> localCapacities;

	public void initFromProperties(Properties spec) {		
	}
	
	public void initFromXml(org.w3c.dom.Node xml) {
	}

	public void initFromJson(JSONObject json) {		
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isLegacy(){
		return legacy;
	}
	
	public boolean isSSLselfSigned(){
		return SSLselfSigned;
	}
	
	public String getHost(){
		return host;
	}
	
	public String getProtocol(){
		return protocol;
	}

	public NodeSpec getNodeSpec() {
		return null;
	}

	public void setNodeSpec(NodeSpec spec) {		
	}

	public int type() {
		return Node.TYPE_SERVER;
	}
	
	public void setLegacy(boolean leg){
		legacy = leg;
	}
	
	public void setHost(String h){
		host = h;
	}
	
	public void setProtocol(String prot){
		protocol = prot;
	}
	
	public void setSelSigned(boolean ssl){
		SSLselfSigned = ssl;
	}
	
	public void setPath(String p){
		path = p;
	}
	
	public String getUrl(){
		return protocol+"://"+host+path+"/";
	}
	
	public String path(){
		return path;
	}
		
	public void addConfig(String name, String value){
		if(localCapacities == null){
			localCapacities = new HashMap<String, String>();
		}
		localCapacities.put(name, value);
	}
	
	public String getRemoteConfig(String name){
		return remoteCapacities.get(name);
	}
	
	public String getLocalConfig(String name){
		if(localCapacities == null) return null;
		return localCapacities.get(name);
	}
	
	
}
