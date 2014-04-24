This document describes the pydio java sdk implementation based on an UML specification in SDK_uml.
The SDK is organised on layers ( HTTP layer, TRANSPORT Layer, PYDIO layer). Each layer provide services to the upper layer.
Beside theses layers, there are the DATA MODEL and some useful functions in UTILS.

				  
				  
1 the DATA MODEL
----------------

the data model describes a bunch of units that are used inside theses layers.
The Node class is the top class of an hierarchy composed of ServerNode, RepositoryNode, FileNode and VirtualNode.
* node types
    * ServerNode 		: Object used to carry all information about the remote pydio server.
    * Repository Node 	: The representation of a remote Repository.
    * FileNode 			: the representation(carries all properties) of remote files.
    * Virtual 			: Specific FileNode.	
Another important class is the Message class that carry information on performed pydio actions.

2 HTTP layer
------------

the HTTP layer provider http services to Transport layer via the Requester class.
The method issueRequest() of the Requester class perform an http request using a given URI object and a Map Object containing Http parameters.

3 The transport Layer
---------------------
The Transport Layer provides simple methods to retrieve/put content form/to the remote pydio server via Transport classes.
This layer also provides 2 ways of communication via RestTranport and SessionTransport classes.
* Transpoort types
    * RestTranport		: To send rest request to remote server.
    * SessionTransport	: To handle a session.
	
4 Pydio Layer
-------------
The Pydio layer provides simple file system actions via the pydioClient class.