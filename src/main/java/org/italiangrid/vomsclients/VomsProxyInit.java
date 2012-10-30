package org.italiangrid.vomsclients;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.credential.CredentialsUtils;
import org.italiangrid.voms.credential.UserCredentials;
import org.italiangrid.voms.request.VOMSACService;
import org.italiangrid.voms.request.impl.DefaultVOMSACRequest;
import org.italiangrid.voms.request.impl.DefaultVOMSACService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.X509Credential;
import eu.emi.security.authn.x509.proxy.ProxyCertificate;
import eu.emi.security.authn.x509.proxy.ProxyCertificateOptions;
import eu.emi.security.authn.x509.proxy.ProxyGenerator;

/**
 * 
 * This class implements a command-line voms-proxy-init client.
 * 
 * @author Daniele Andreotti
 * 
 */

public class VomsProxyInit {

	private String vo;
	private int keyLength = 1024;

	private String keyPassword;
	private String proxyOutput;

	private static final Logger log = LoggerFactory.getLogger(VomsProxyInit.class);

	/** The CLI options **/
	private static final Options cliOptions = new Options();

	/** The CLI options parser **/
	private static final CommandLineParser cliParser = new GnuParser();


	@SuppressWarnings("static-access")
	private void initOptions() {

		cliOptions.addOption("help", "usage", false, "Displays helps and exits");

		cliOptions.addOption(OptionBuilder.withLongOpt("vomsdir")
				.withDescription("Specifies non-standard vomsdir directory.").hasArg().create("vomsdir"));

		cliOptions.addOption(OptionBuilder.withLongOpt("cadir")
				.withDescription("Specifies non-standard ca certificate directory.").hasArg(true).create("cadir"));

		cliOptions
				.addOption(OptionBuilder
						.withLongOpt("vomsesPath")
						.withDescription(
								"Specifies non-standard locations where the voms-proxy-init looks for vomses files. The path is a colon (:) separated list of paths.")
						.hasArg().create("vomsesPath"));


		cliOptions.addOption(OptionBuilder.withLongOpt("usercert")
				.withDescription("Specifies non-standard user certificate.").hasArg().create("usercert"));

		cliOptions.addOption(OptionBuilder.withLongOpt("userkey")
				.withDescription("Specifies non-standard user private key.").hasArg().create("userkey"));


		cliOptions.addOption(OptionBuilder.withLongOpt("password")
				.withDescription("Specifies a password that is used to decrypt the user's private key.").hasArg()
				.create("password"));

		cliOptions.addOption(OptionBuilder.withLongOpt("lifetime")
				.withDescription("Specifies the lifetime for the generated proxy.").hasArg().create("lifetime"));

		cliOptions
				.addOption(OptionBuilder
						.withLongOpt("out")
						.withDescription(
								"Specifies a non-standard location for the generated proxy. The standard location is /tmp/X509_up_<username>.")
						.hasArg().create("out"));


		cliOptions.addOption(OptionBuilder
				.withLongOpt("order")
				.withDescription(
						"Specifies the ordering of received attributes. The options is a comma (,) separated list of FQANs.")
				.hasArg().create("order"));

		cliOptions.addOption(OptionBuilder.withLongOpt("voms")
				.withDescription("Specifies a request FQAN in the form: <voName>:<FQAN>.").hasArgs().create("voms"));

		cliOptions.addOption(OptionBuilder.withLongOpt("targets")
				.withDescription("Targets the AC against a specific comma separated list of hostnames.").hasArg()
				.create("targets"));

		cliOptions
				.addOption(OptionBuilder
						.withLongOpt("proxyType")
						.withDescription(
								"Specifies the type of proxy that will be generated. Possible values are: GT2_PROXY, GT3_PROXY, GT4_PROXY. The default value is GT2_PROXY.")
						.hasArg().create("proxyType"));

		cliOptions.addOption(OptionBuilder.withLongOpt("policyType")
				.withDescription("Specifies the policy type of the proxy.  Only significant with proxyType >= GT3_PROXY.")
				.hasArg().create("policyType"));

		cliOptions
				.addOption(OptionBuilder
						.withLongOpt("delegationType")
						.withDescription(
								"Specifies the type of delegation requested for the generated proxy. Possible values are: NONE, LIMITED, FULL. The default value is FULL.")
						.hasArg().create("delegationType"));

		cliOptions
				.addOption(OptionBuilder
						.withLongOpt("bits")
						.withDescription(
								"Specifies the key size of the created proxy. Possible values are 512, 1024, 2048.  The default alue is 1024")
						.hasArg().create("bits"));
	}


	private void parseCommandLineOptions(String[] args) {
		try {
			CommandLine line = cliParser.parse(cliOptions, args);

			if (line.hasOption("help") || line.hasOption("usage")) {
				usage();
				System.exit(0);
			}


			if (line.hasOption("out"))
				proxyOutput = line.getOptionValue("out");

			if (line.hasOption("voms"))
				vo = line.getOptionValue("voms");

			if (line.hasOption("password"))
				keyPassword = line.getOptionValue("password");

			if (line.hasOption("bits"))
				keyLength = Integer.parseInt(line.getOptionValue("bits"));

		} catch (ParseException e) {
			log.error(e.getMessage(), e.getCause());
			System.err.println("Error parsing command line arguments: " + e.getMessage());
			usage();
			System.exit(1);
		}
	}


	private void generateProxy() {

		X509Credential credentials = UserCredentials.loadCredentials(keyPassword.toCharArray());

		DefaultVOMSACRequest request = new DefaultVOMSACRequest();

		request.setVoName(vo);


		VOMSACService service = new DefaultVOMSACService();

		AttributeCertificate attributeCertificate = service.getVOMSAttributeCertificate(credentials, request);

		ProxyCertificateOptions proxyOptions = new ProxyCertificateOptions(credentials.getCertificateChain());
		proxyOptions.setAttributeCertificates(new AttributeCertificate[] { attributeCertificate });

		proxyOptions.setKeyLength(keyLength);


		try {
			/* Proxy generation */
			ProxyCertificate proxyCert = ProxyGenerator.generate(proxyOptions, credentials.getKey());

			/* Save the proxy */
			CredentialsUtils.saveCredentials(new FileOutputStream(proxyOutput), proxyCert.getCredential());

		} catch (InvalidKeyException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (CertificateParsingException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (SignatureException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (CertificateException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (UnrecoverableKeyException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (KeyStoreException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (NoSuchProviderException e) {
			log.error(e.getMessage(), e.getCause());
		} catch (IOException e) {
			log.error(e.getMessage(), e.getCause());
		}

	}


	/**
	 * Prints usage information
	 */
	static void usage() {

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("VomsProxyInit", cliOptions);
	}


	public VomsProxyInit(String[] args) {
		initOptions();
		parseCommandLineOptions(args);
		generateProxy();
	}
}
