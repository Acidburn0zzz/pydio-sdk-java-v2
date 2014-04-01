package io.pyd.sdk.client.utils;

import io.pyd.sdk.client.model.ServerNode;


/**
 * Interface describing class to resolve server adress
 * @author pydio
 *
 */
public interface ServerResolver {
	public final static String SERVER_URL_RESOLUTION = "RequestResolution";
	void resolveServer(ServerNode server);
}
