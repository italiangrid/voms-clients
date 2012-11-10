package org.italiangrid.voms.clients.options.v2;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.italiangrid.voms.clients.options.v2.VOMSCLIOptionBuilder.BundleKey;

public enum CLIOptionsBundle {
	common, proxyInit, proxyInfo, proxyDestroy;

	private CLIOptionsBundle() {
		String bundleName = VOMSCLIOptionBuilder.class.getPackage()
				.getName() + "." + this.name() + "Options";
		bundle = ResourceBundle.getBundle(bundleName);
		if (bundle == null)
			throw new IllegalStateException(
					"Cannot load VOMS CLI options: options bundle not found: "
							+ bundleName);
	}

	private ResourceBundle bundle;

	public ResourceBundle getBundle() {
		return bundle;
	}

	public String getStringFromBundle(String longOpt, BundleKey key) {
		String returnValue = null;

		try {

			returnValue = getBundle().getString(longOpt + "." + key.name());

		} catch (MissingResourceException e) {
			// Swallow exception
		}

		return returnValue;
	}
}