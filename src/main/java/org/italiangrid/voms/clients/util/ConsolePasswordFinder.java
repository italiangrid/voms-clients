package org.italiangrid.voms.clients.util;

import java.io.Console;

import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.VOMSError;

/**
 * 
 * @author andreaceccanti
 * 
 */
public class ConsolePasswordFinder implements PasswordFinder {

	private String promptMessage;
	
	public ConsolePasswordFinder(String prompt) {
		this.promptMessage = prompt;
	}

	public char[] getPassword() {
		Console console = System.console();

		if (console == null)
			throw new VOMSError(
					"Error obtaining password from console: no console found for this JVM!");

		return console.readPassword(promptMessage);
	}

}
