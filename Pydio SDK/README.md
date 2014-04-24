# PYDIO JAVA SDK

This is the Pydio java SDK which you can use in your application to communicate with Pydio servers in unified and quick way. This SDK encapsulates queries which you would have to send to server. It simplifies authorization logic and lets you operate on objects representing server data structures.

**If you would like to contribute to the pydio java SDK, you are very welcome** :)

## Initializing project
The project uses third party libraries that are already referenced in the pom.xml file. So you have cloned and imported the project you have nothing to do but make sure that Maven is enable in your IDE.

## How to add the Pydio java SDK to your project
To add the Pydio java sdk to your project, you only need to make it referenced by your project. For example if you are using eclipse, follow theses steps:
	- Right-click on your project --> Properties --> Project references --> select the Pydio java SDK --> OK
 

## Usage instructions 

To use the SDK, there are three basic classes in SDK you need to know

 - ServerNode  : holds information (host name, path, protocol) about the pydio server you target.
 - PydioClient : is the class that allow you to remotely perform file system operation on your pydio server.
 - StateHolder : holds parameters set by user of the library. StateHolder holds some defaults parameters that can be changed.

### How to simply get the repository list of a pydio server
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

### How authentication is performed
Authentication to server is asked at the very needed moment and authentication data are provided via a CredentialsProvider instance. The CommandLineCredentialsProvider is set by default in the StateHolder. So when performing your repository listing your login and password are asked in console. 
You can use your own credentials provider by implementing the CredentialsProvider interface this way :

```java
StateHolder.getInstance().setCredentialsProvider(new CredentialsProvider{
		public UsernamePasswordCredentials requestForLoginPassword(){
			// do your stuff here
		}
		
		public X509Certificate requestForCertificate(){
			// do your stuff here
		}	
});
```

**You can find more examples in 'examples' folder**

## Architecture of library
The Pydio java SDK is organised on layers ( from bottom :  HTTP layer, TRANSPORT Layer, PYDIO layer) as showed in the SDK_uml image. Each layer provide service to the upper layer.
Beside theses layers, there are the DATA MODEL and some useful functions in UTILS.



###The data model
the data model describes a bunch of units that are used inside theses layers.
The Node class is the top class of an hierarchy composed of ServerNode, RepositoryNode, FileNode and VirtualNode.
* node types :
    * ServerNode 	: Object used to carry all information about the remote pydio server.
    * Repository Node 	: The representation of a remote Repository.
    * FileNode 		: the representation(carries all properties) of remote files.
    * Virtual 		: Specific FileNode.	
Another important class is the Message class that carry information on performed pydio actions.

###The HTTP layer
the HTTP layer provider http services to Transport layer via the Requester class.
The method issueRequest() of the Requester class perform an http request using a given URI object and a Map Object containing Http parameters.

###The Transport Layer
The Transport Layer provides simple methods to retrieve/put content form/to the remote pydio server via Transport classes.
This layer also provides 2 ways of communication via RestTranport and SessionTransport classes.
* Transpoort types :
    * RestTranport      : To send rest request to remote server.
    * SessionTransport	: To handle a session.
	
###Pydio Layer
The Pydio layer provides simple file system actions via the pydioClient class.



## How to contribute

If you like, you can add new not implemented operations, for example described [here][3].
Please <a href="http://pyd.io/contribute/cla">sign the Contributor License Agreement</a> before your PR can be merged.


 [0]: https://github.com/AFNetworking/AFNetworking
 [1]: https://github.com/jonreid/OCMockito
 [2]: https://github.com/hamcrest/OCHamcrest
 [3]: http://pyd.io/resources/serverapi/#!/access.fs



