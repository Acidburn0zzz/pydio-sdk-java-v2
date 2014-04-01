
package io.pyd.sdk.client.transport;

import io.pyd.sdk.client.http.CountingMultipartRequestEntity;

import java.io.File;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.w3c.dom.Document;

/**
 * 
 * @author pydio
 *
 */
public interface Transport{
	
	/**
	 * Constant given to TransportFactory to generate a SessionTransport instance. 
	 */
	public static int MODE_SESSION = 1;
	
	/**
	 * Constant given to TransportFactory to generate a SessionTransport instance. 
	 */
	public static int MODE_RESTFUL = 2;
	
	/**
	 * Constant given to TransportFactory to generate a SessionTransport instance. 
	 */
	public static int MODE_MOCK = 3;
	
	
	/**
	 * Performs a Pydio action. 	
	 * @param action Pydio class action constant
	 * @param params A map of parameters that go with the action
	 * @return
	 */
	public HttpResponse getResponse(String action, Map<String, String> params);
	
	
	
	/**
	 * Performs a Pydio action on the remote server and return the result as a text
	 * @param action Pydio class action constant
	 * @param params A map of parameters that go with the action
	 * @return The response as a String 
	 */
	public String getStringContent(String action, Map<String, String> params);
	
	
	
	/**
	 * Performs a Pydio action on the remote server and return the result in xml format
	 * @param action Pydio class action constant
	 * @param params A map of parameters that go with the action
	 * @return An XML Document object
	 */
	public Document getXmlContent(String action, Map<String, String> params);
	
	
	
	/**
	 * Performs a Pydio action on the remote server and return the result as json format
	 * @param action Pydio class action constant
	 * @param params A map of parameters that go with the action
	 * @return A JSONObject
	 */
	public JSONObject getJsonContent(String action, Map<String, String> params);
	
	
	
	/**
	 * Performs an file upload 
	 * @param action Pydio class action constant
	 * @param params A map of parameters that go with the action
	 * @param file The local file which content is to be uploaded
	 * @param filename The remote file name if different of the local file one
	 * @param listener A progress listener
	 * @return An XML Document object
	 */
	public Document putContent(String action, Map<String, String> params, File file, String filename, CountingMultipartRequestEntity.ProgressListener listener);
		
	
	/**
	 * Add raw data in a remote file.
	 * @param action Pydio class action constant
	 * @param params A map of parameters that go with the action
	 * @param data Raw data to put on the server
	 * @param filename The name of the remote file to put data in
	 * @param listener A progress listener 
	 * @return An XML Document object
	 */
	public Document putContent(String action, Map<String, String> params, byte[] data, String filename, CountingMultipartRequestEntity.ProgressListener listener);
	
}