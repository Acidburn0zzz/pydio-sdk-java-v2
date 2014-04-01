package io.pyd.sdk.client.transport;

import io.pyd.sdk.client.auth.CredentialsProvider;
import io.pyd.sdk.client.utils.StateHolder;

public class TransportFactory {

	/**
	 * generate a concrete Transport object to the mode
	 * @param mode int value of the mode
	 * @param provider A instance of CredentialsProvider used for authentication
	 * @return null if the mode value is unknown
	 */
	public static Transport getInstance(int mode, CredentialsProvider provider){		
		
		if(mode == Transport.MODE_RESTFUL){
			return new RestTransport();
		}else if(mode == Transport.MODE_SESSION){
			return new SessionTransport(StateHolder.getInstance().getServer(), provider);
		}else if(mode == Transport.MODE_MOCK){
			return new MockTransport();
		}		
		return null;
	}
}
