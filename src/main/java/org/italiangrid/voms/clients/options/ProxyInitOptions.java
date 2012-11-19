package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

public enum ProxyInitOptions implements CLIOption{

	AC_LIFETIME("vomslife"),
	VALIDITY("valid"),
	CERT_LOCATION("cert"),
	ENABLE_STDIN_PWD("pwstdin"),
	FAIL_ON_WARN("failonwarn"),
	FQANS_ORDERING("order"),
	IGNORE_WARNINGS("ignorewarn"),
	KEY_SIZE("bits"),
	KEY_LOCATION("key"),
	LEGAGY_PROXY("old"),
	LIMITED_PROXY("limited"),
	OPTIONS_FILE("conf"),
	PATHLEN_CONSTRAINT("path_length"),
	PROXY_LOCATION("out"),
	PROXY_LIFETIME_IN_HOURS("hours"),
	PROXY_NOREGEN("noregen"),
	PROXY_VERSION("proxyver"),
	QUIET_MODE("quiet"),
	REQUEST_TIMEOUT("timeout"),
	RFC_PROXY("rfc"),
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
