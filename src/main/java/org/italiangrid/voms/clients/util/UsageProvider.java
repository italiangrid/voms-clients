package org.italiangrid.voms.clients.util;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * Helper for displying usage in command line.
 * 
 * 
 * @author valerioventuri
 *
 */
public class UsageProvider {

  /**
   * Displays usage.
   * 
   * @param cmdLineSyntax the string that will be displayed on top of the usage message
   * @param options the command options
   */
  public static void displayUsage(String cmdLineSyntax, Options options) {
    
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp(cmdLineSyntax, options);
  }
  
}
