pydio-sdk-java-v2
=================

# PYDIO JAVA SDK

This is the iOS Pydio Server API wrapper which you can use in your application to communicate with Pydio servers in unified and quick way. This SDK encapsulates, in form of Objective-C messages, queries which you would have to send to server. It simplifies authorization logic and lets you operate on objects representing server data structures.

**If you would like to contribute to the pydio java SDK, you are very welcome** :)

## Initializing project
Project uses third party libraries that are already referenced in the pom.xml file. So you have cloned and imported the project you have nothing to do but make sure that Maven is enable in your IDE.

## How to add the Pydio java SDK to your project
To add the Pydio java sdk to your project, just make it referenced by your project. For example if you are using eclipse, follow theses steps:
	- Right-click on your project --> Properties --> Project references --> select the Pydio java SDK --> OK
 

## Usage instructions 

To quickly use the SDK there are three basic classes in SDK you need to know

	- ServerNode  : holds information (host name, path, protocol) about the pydio server you target
	- PydioClient : is the class that allow you to perform file system operation on your pydio server
	- StateHolder : holds parameters set by user of the library. StateHolder holds some defaults parameters that can be changed.
 
Here are steps that show how to get the repository list of a server. The first thing you hava to do is to create a configure your server :
```java
String address = "localhost", path = "/pydio", protocol = "http";
ServerNode server = (ServerNode) NodeFactory.createNode(Node.TYPE_SERVER);
server.setHost(address);
server.setPath(path);
server.setProtocol(protocol);
```

Once you have your target server initialized, set it as your current server so your PydioClient can automatically use it :
```java
StateHolder.getInstance().setServer(server);
```

Now get your PydioClient instance and list repository 
```java
PydioClient pydio = new PydioClient();
//list the server repositories
ArrayList<RepositoryNode> repositories = pydio.listRepository();
```
