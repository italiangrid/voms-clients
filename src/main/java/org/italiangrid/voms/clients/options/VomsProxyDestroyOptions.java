package org.italiangrid.voms.clients.options;

public enum VomsProxyDestroyOptions implements VomsCliOption {

  HELP('h', "help", "Displays usage"),

  USAGE('u', "usage", "Displays version"),

  VERSION('v', "version", "Displays version"),

  DEBUG('d', "debug", "Enables extra debug output"),
  
  QUIET('q', "quiet", "Quiet mode, minimal output"),
  
  FILE('f', "file", "Specifies proxy file name"),
  
  DRY("dry", "Only go in dryrun mode"),
  
  CONF('c', "conf", "Load options from file <arg>");

  private final String opt;
  private final String longOpt;
  private final String description;
  private final boolean hasArg;
  private final String argDescription;
  
  private VomsProxyDestroyOptions(String longOpt, String description) {
    this.opt = null;
    this.longOpt = longOpt;
    this.description = description;
    this.hasArg = false;
    this.argDescription = "";
  }

  private VomsProxyDestroyOptions(char opt, String longOpt, String description) {
    this.opt = Character.toString(opt);
    this.longOpt = longOpt;
    this.description = description;
    this.hasArg = false;
    this.argDescription = "";
  }

  private VomsProxyDestroyOptions(String longOpt, String description, String argDescription) {
    this.opt = null;
    this.longOpt = longOpt;
    this.description = description;
    this.hasArg = true;
    this.argDescription = argDescription;
  }

  private VomsProxyDestroyOptions(char opt, String longOpt, String description, String argDescription) {
    this.opt = Character.toString(opt);
    this.longOpt = longOpt;
    this.description = description;
    this.hasArg = true;
    this.argDescription = argDescription;
  }

  public String getOpt() {
    return opt;
  }

  public String getLongOpt() {
    return longOpt;
  }

  public String getDescription() {
    return description;
  }

  public boolean hasArg() {
    return hasArg;
  }

  public String getArgDescription() {
    return argDescription;
  }

}
