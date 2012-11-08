package org.italiangrid.voms.clients.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.VOMSError;

public class InputStreamPasswordFinder implements PasswordFinder {

	InputStream is;
	PrintStream os;

	public InputStreamPasswordFinder(InputStream is, OutputStream os) {
		this.is = is;
		this.os = new PrintStream(os);
	}

	public char[] getPassword() {
		try {

			os.print("Enter private key password:");
			os.flush();
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String passwordLine = reader.readLine();
			if (passwordLine != null)
				return passwordLine.toCharArray();
			return null;

		} catch (IOException e) {

			throw new VOMSError("Error reading password from input stream: "
					+ e.getMessage(), e);
		}
	}

}