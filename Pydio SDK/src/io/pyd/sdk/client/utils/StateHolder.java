package io.pyd.sdk.client.utils;

import java.util.HashMap;
import java.util.Map;

import io.pyd.sdk.client.model.FileNode;
import io.pyd.sdk.client.model.RepositoryNode;
import io.pyd.sdk.client.model.ServerNode;


/**
 * 
 * @author pydio
 *
 */
public class StateHolder {
	
	ServerNode currentServer;
	RepositoryNode currentRepository;
	FileNode currentDirectory;
	private static StateHolder holder = null;
	private Map<String, String> localConfigs;
	
	private EventBus bus;
	/** 
	 * @return the unique instance of the StaeHolder class
	 */
	public static StateHolder getInstance(){
		if(holder == null){
			holder = new StateHolder();
			holder.localConfigs = new HashMap<String, String>();
			holder.localConfigs.put(Pydio.LCONFIG_BUFFER_SIZE, Pydio.LCONFIG_BUFFER_SIZE_DVALUE+"");
		}
		return holder;
	}
	
	
	/**
	 * retrieves the current repository
	 * @return a RepsoitoryNode Object
	 */
	public RepositoryNode getRepository(){
		return currentRepository;
	}
	
	
	/**
	 * retrieves the current directory
	 * @return a FileNode Object
	 */
	public FileNode getDirectory(){
		return currentDirectory;
	}
	
	
	/**
	 * retrieves the current server
	 * @return a ServerNode
	 */
	public ServerNode getServer(){
		return currentServer;
	}
	
	
	/**
	 * set the current Server
	 * @param server a ServerNode Object
	 */
	public void setServer(ServerNode server){
		currentServer = server;
	}
	
	
	/**
	 * set the current directory 
	 * @param directory a FileNode object
	 */
	public void setDirectory(FileNode directory){
		currentDirectory = directory;
	}
	
	
	/**
	 * set the current repository
	 * @param repository RepositoryNode
	 */
	public void setRepository(RepositoryNode repository){
		currentRepository = repository;
	}
	
	
	/**
	 * @return true if a server is set, false if not
	 */
	boolean isServerSet(){
		return currentServer != null;
	}
	
	
	/**
	 * @return true is a repository is set, false if not
	 */
	boolean isRepositorySet(){
		return currentRepository != null;
	}
	
	public String getLocalConfig(String name){
		if(localConfigs == null){
			//loadLocalConfigs
		}
		return localConfigs.get(name);
	}
	
	public void addConfig(String name, String value){
		localConfigs.put(name, value);
	}	
	
	public void setBus(EventBus bus){
		this.bus = bus;
	}
	
	public EventBus bus(){
		if(bus == null){
			bus = DefaultEventBus.bus();
		}
		return bus;
	}
}
