/**
 * 
 */
package io.pyd.sdk.client.auth;

import java.security.cert.X509Certificate;

import org.apache.http.auth.UsernamePasswordCredentials;


/**
 * Interface describing callbacks to provide authentication items
 * @author pydio
 *
 */
public interface CredentialsProvider {
	
	public UsernamePasswordCredentials requestForLoginPassword();
	public X509Certificate requestForCertificate();	
}

