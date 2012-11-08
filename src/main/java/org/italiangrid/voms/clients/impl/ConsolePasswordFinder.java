package org.italiangrid.voms.clients.impl;

import java.io.Console;

import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.VOMSError;

/**
 * 
 * @author andreaceccanti
 * 
 */
public class ConsolePasswordFinder implements PasswordFinder {

	public ConsolePasswordFinder() {

	}

	public char[] getPassword() {
		Console console = System.console();

		if (console == null)
			throw new VOMSError(
					"Error obtaining password from console: no console found for this JVM!");

		return console.readPassword("Enter private key password:");
	}

}
