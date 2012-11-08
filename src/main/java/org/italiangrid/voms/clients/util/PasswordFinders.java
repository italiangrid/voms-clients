package org.italiangrid.voms.clients.util;

import java.io.InputStream;
import java.io.OutputStream;

import org.bouncycastle.openssl.PasswordFinder;

public class PasswordFinders {

	public static PasswordFinder getDefault() {
		if (System.console() != null)
			return new ConsolePasswordFinder();

		return new InputStreamPasswordFinder(System.in, System.out);
	}

	public static PasswordFinder getConsolePasswordFinder() {
		return new ConsolePasswordFinder();
	}

	public static PasswordFinder getInputStreamPasswordFinder(InputStream is, OutputStream os) {
		return new InputStreamPasswordFinder(is, os);
	}

}
