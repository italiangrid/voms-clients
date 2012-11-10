package org.italiangrid.voms.clients.options.v2;

import org.apache.commons.cli.Option;


public class VOMSCLIOptionBuilder {

	enum BundleKey {
		opt, description, hasArg, argDescription
	}

	public static Option buildOption(String longOpt, CLIOptionsBundle b) {

		String shortOpt = b.getStringFromBundle(longOpt, BundleKey.opt);
		String description = b.getStringFromBundle(longOpt,
				BundleKey.description);
		boolean hasArg = false;

		if (b.getStringFromBundle(longOpt, BundleKey.hasArg) != null)
			hasArg = Boolean.parseBoolean(b.getStringFromBundle(longOpt,
					BundleKey.hasArg));

		String argDescription = b.getStringFromBundle(longOpt,
				BundleKey.argDescription);

		Option o = new Option(shortOpt, longOpt, hasArg, description);
		o.setArgName(argDescription);
		return o;
		
	}

	private VOMSCLIOptionBuilder() {
	}
}
