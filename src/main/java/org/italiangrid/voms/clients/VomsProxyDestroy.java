package org.italiangrid.voms.clients;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.italiangrid.voms.clients.options.VomsCliOption;
import org.italiangrid.voms.clients.options.VomsProxyDestroyOptions;
import org.italiangrid.voms.clients.util.OptionsFileLoader;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.italiangrid.voms.clients.util.UsageProvider;
import org.italiangrid.voms.clients.util.VersionProvider;
import org.italiangrid.voms.credential.ProxyPathBuilder;
import org.italiangrid.voms.credential.impl.DefaultProxyPathBuilder;

/**
 * Class implementing voms-proxy-destroy.
 * 
 * @author valerioventuri
 * 
 */
public class VomsProxyDestroy {

	/**
	 * Commons CLI {@link Options} object for holding a set of command line
	 * options.
	 * 
	 */
	private Options options = new Options();

	/**
	 * Commons CLI {@link CommandLine} object for holding options after parsing,
	 * and get them in the code.
	 * 
	 */
	private CommandLine commandLine;

	/**
	 * Whether the command should produce extra output for debug purposes.
	 * 
	 */
	private boolean debug;

	/**
	 * Whether the command should produce a minimal output.
	 * 
	 */
	private boolean quiet;

	/**
	 * Whether the command should run in dry mode.
	 * 
	 */
	private boolean dryRun;

	/**
	 * Name of the proxy certificate file.
	 */
	private String proxyFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new VomsProxyDestroy(args);
	}

	public VomsProxyDestroy(String[] args) {

		initOptions();
		parseOptions(args);
		doDestroy();
	}

	private void initOptions() {

    List<VomsCliOption> listOptions = new ArrayList<VomsCliOption>();
    listOptions.addAll(Arrays.asList(VomsProxyDestroyOptions.values()));

    for (VomsCliOption optionElement : listOptions) {

      Option option = new Option(optionElement.getOpt(), optionElement.getLongOpt(), 
          optionElement.hasArg(), optionElement.getDescription());

      option.setDescription(optionElement.getArgDescription());
      
      options.addOption(optionElement.getOpt(),
          optionElement.getLongOpt(),
          optionElement.hasArg(),
          optionElement.getDescription());
    }
	
	}

	private void parseOptions(String[] args) {

		CommandLineParser commandLineParser = new GnuParser();

		try {

			commandLine = commandLineParser.parse(options, args);

			if (commandLine.hasOption("conf")) {

				String optionFile = commandLine.getOptionValue("conf");

				String[] reloadedArgs = OptionsFileLoader.loadOptions(args,	
				    optionFile);

				commandLine = commandLineParser.parse(options, reloadedArgs);
			}

			if (commandLine.hasOption("help")) {

			  UsageProvider.displayUsage("voms-proxy-destroy", options);

				System.exit(0);
			}

			if (commandLine.hasOption("version")) {

				VersionProvider.displayVersionInfo("voms-proxy-destoy");

				System.exit(0);
			}

			debug = commandLine.hasOption("debug");

			quiet = commandLine.hasOption("quiet");

			dryRun = commandLine.hasOption("dry");

			proxyFile = commandLine.getOptionValue("file");

		} catch (ParseException e) {

			System.err.println("voms-proxy-destroy: " + e.getMessage());

		   UsageProvider.displayUsage("voms-proxy-destroy", options);

			System.exit(1);
		}

	}

	private void doDestroy() {

		if (proxyFile == null) {

			proxyFile = VOMSProxyPathBuilder.buildProxyPath();
		}

		File file = new File(proxyFile);

		if (!file.exists()) {

			if (!quiet) {

				System.err
						.println("\nProxy file doesn't exist or has bad permissions\n");
			}

			System.exit(1);
		}

		if (dryRun) {

			System.out.println("Would remove " + proxyFile);

			System.exit(0);
		}

		file.delete();
	}

}
