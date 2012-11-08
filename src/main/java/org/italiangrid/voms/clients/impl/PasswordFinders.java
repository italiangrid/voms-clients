package org.italiangrid.voms.clients.impl;

import java.io.InputStream;

import org.bouncycastle.openssl.PasswordFinder;

public class PasswordFinders {

	public static PasswordFinder getDefault() {
		if (System.console() != null)
			return new ConsolePasswordFinder();

		return new InputStreamPasswordFinder(System.in);
	}

	public static PasswordFinder getConsolePasswordFinder() {
		return new ConsolePasswordFinder();
	}

	public static PasswordFinder getInputStreamPasswordFinder(InputStream is) {
		return new InputStreamPasswordFinder(is);
	}

}
