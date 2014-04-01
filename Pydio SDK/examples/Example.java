package io.pyd.sdk.client;

import io.pyd.sdk.client.auth.CommandlineCredentialsProvider;
import io.pyd.sdk.client.model.FileNode;
import io.pyd.sdk.client.model.Node;
import io.pyd.sdk.client.model.NodeFactory;
import io.pyd.sdk.client.model.RepositoryNode;
import io.pyd.sdk.client.model.ServerNode;
import io.pyd.sdk.client.transport.Transport;
import io.pyd.sdk.client.utils.StateHolder;

import java.util.ArrayList;

/**
 * Example of listing a directory
 * @author pydio
 *
 */
public class Example {
	
	public static void main(String[] args) {

		String address = "192.168.0.181", path = "test", protocol = "http";
		boolean isLegacy = false;
		

		
		//create a server and initialize it
		ServerNode server = (ServerNode) NodeFactory.createNode(Node.TYPE_SERVER);
		server.setHost(address);
		server.setPath(path);
		server.setProtocol(protocol);
		server.setLegacy(isLegacy);

		//set it as the current server so your the pydio client can automatically work on
		StateHolder.getInstance().setServer(server);

		
		//instantiate a pydio client in Session mode with a CommandlineCredentialsProvider object
		PydioClient pydio = new PydioClient(Transport.MODE_SESSION,	new CommandlineCredentialsProvider());
		
		//list the server repositories
		ArrayList<RepositoryNode> repos = pydio.listRepository();		
		
		// displaying list of repository ID and accesType
		System.out.println("\n--Repository list (ID | ACCESS_TYPE):\n");
		for (int i = 0; i < repos.size(); i++) {
			RepositoryNode repo = (RepositoryNode) repos.get(i);
			System.out.println(repo.getId() + " | " + repo.getAccesType());
		}
		
		//we get the default repository
		RepositoryNode repo = repos.get(0);
				
		//we list the first repository children
		ArrayList<Node> children = pydio.listChildren(repo, null, null);
		
		//displaying children
		System.out.println("\n--Repository list (NAME | SIZE) :\n");
		for (int i = 0; i < children.size(); i++) {
			FileNode child = (FileNode) children.get(i);
			System.out.println(child.path() + " | " + child.size());
		}
	}
}
