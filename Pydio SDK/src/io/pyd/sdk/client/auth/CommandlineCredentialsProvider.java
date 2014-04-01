package io.pyd.sdk.client.auth;

import java.security.cert.X509Certificate;
import java.util.Scanner;

import org.apache.http.auth.UsernamePasswordCredentials;

/**
 * A credentials provider that prompt user to get login and password 
 * @author pydio
 *
 */
public class CommandlineCredentialsProvider implements CredentialsProvider {

	public UsernamePasswordCredentials requestForLoginPassword() {
		System.out.println("\n\n��������������Authentication������������������");
		String login , password;
		System.out.print("login    :");
		login = new Scanner(System.in).nextLine();
		System.out.print("password :");
		password = new Scanner(System.in).nextLine();
		System.out.println("����������������������������������������������");
		return new UsernamePasswordCredentials(login, password);
	}

	public X509Certificate requestForCertificate() {
		// TODO Auto-generated method stub
		return null;
	}

}
