package org.italiangrid.vomsclients;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class VomsProxyDestroy {

  private static Options cliOptions = new Options();
  
  private static CommandLine commandLine;
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    
    new VomsProxyDestroy(args);
  }

  public VomsProxyDestroy(String[] args) {
    
    initOptions();
    parseOptions(args);
  }
  
  private void initOptions() {
    
    cliOptions.addOption("h", "help", false, "Displays usage");
    cliOptions.addOption("v", "version", false, "Displays version");
    cliOptions.addOption("d", "debug", false, "Enables extra debug output");
    cliOptions.addOption("f", "file", true, "Specifies proxy file name");
    cliOptions.addOption("d", "dry", true, "Only go in dryrun mode");
    cliOptions.addOption("c", "conf", true, "Load options from file <file>");
    cliOptions.addOption("q", "quiet", true, "Quiet mode, minimal output");
  }

  private void parseOptions(String[] args) {
    
    CommandLineParser commandLineParser = new GnuParser();
    
    try {
      
      commandLine = commandLineParser.parse(cliOptions, args);
    

      if (commandLine.hasOption("help")) {

        displaysUsage();
        
        System.exit(0);
      }

      
    } catch (ParseException e) {

      System.err.println("Error parsing command line arguments: " + e.getMessage());
      
      displaysUsage();
      
      System.exit(1);
    }
    
  }

  private void displaysUsage() {
    
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("VomsProxyDestroy", cliOptions);
  }
  
}
