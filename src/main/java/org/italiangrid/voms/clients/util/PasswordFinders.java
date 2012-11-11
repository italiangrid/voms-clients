package org.italiangrid.voms.clients.util;

import java.io.InputStream;
import java.io.OutputStream;

import org.bouncycastle.openssl.PasswordFinder;

public class PasswordFinders {

	public static final String PROMPT_MESSAGE = "Enter GRID pass phrase for this identity:";
	
	public static PasswordFinder getDefault() {
		if (System.console() != null)
			return new ConsolePasswordFinder(PROMPT_MESSAGE);

		return new InputStreamPasswordFinder(PROMPT_MESSAGE, System.in, System.out);
	}

	public static PasswordFinder getConsolePasswordFinder() {
		return new ConsolePasswordFinder(PROMPT_MESSAGE);
	}

	public static PasswordFinder getInputStreamPasswordFinder(InputStream is, OutputStream os) {
		return new InputStreamPasswordFinder(PROMPT_MESSAGE, is, os);
	}
	
	public static PasswordFinder getNoPromptInputStreamPasswordFinder(InputStream is, OutputStream os) {
		return new InputStreamPasswordFinder(null, is, os);
	}

}
