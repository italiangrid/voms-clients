package org.italiangrid.voms.clients.options.v2;

import org.apache.commons.cli.Option;

public enum ProxyInitOptions implements CLIOption{

	AC_LIFETIME("vomslife"),
	AC_VALIDITY("valid"),
	CERT_LOCATION("cert"),
	ENABLE_STDIN_PWD("pwstdin"),
	FAIL_ON_WARN("failonwarn"),
	FQANS_ORDERING("order"),
	GLOBUS_VERSION("globus"),
	IGNORE_WARNINGS("ignorewarn"),
	INCLUDE_AC_FROM_FILE("includeac"),
	INCLUDE_FILE("include"),
	KEY_LENGTH("bits"),
	KEY_LOCATION("key"),
	LEGAGY_PROXY("old"),
	LIMITED_PROXY("limited"),
	LIST_ALL_ATTRIBUTES("list"),
	OPTIONS_FILE("conf"),
	PATHLEN_CONSTRAINT("path_length"),
	POLICY_FILE("policy"),
	POLICY_LANGUAGE("pl"),
	PROXY_FILENAME("out"),
	PROXY_LIFETIME_IN_HOURS("hours"),
	PROXY_NOREGEN("noregen"),
	PROXY_VERSION("proxyver"),
	QUIET_MODE("quiet"),
	REQUEST_TIMEOUT("timeout"),
	RFC_PROXY("rfc"),
	SAVE_AC_IN_FILE("separate"),
	SKIP_AC_VERIFICATION("dont_verify_ac"),
	TARGET_HOSTNAME("target"),
	TRUSTED_CERT_LOCATION("certdir"),
	VERIFY_CERT("verify"),
	VOMS_COMMAND("voms"),
	VOMSES_LOCATION("vomses");
	
	private Option option;
	
	private ProxyInitOptions(String longOpt){
		option = VOMSCLIOptionBuilder.buildOption(longOpt, CLIOptionsBundle.proxyInit);
	}

	@Override
	public Option getOption() {
		return option;
	}

	@Override
	public String getLongOptionName() {
		
		return option.getLongOpt();
	}
}
