package org.italiangrid.voms.clients.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.VOMSError;

public class InputStreamPasswordFinder implements PasswordFinder {

	InputStream is;

	public InputStreamPasswordFinder(InputStream is) {
		this.is = is;
	}

	public char[] getPassword() {
		try {

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
