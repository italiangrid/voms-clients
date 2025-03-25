// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.options.CLIOption;
import org.italiangrid.voms.clients.options.CommonOptions;
import org.italiangrid.voms.clients.options.ProxyInitOptions;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.OptionsFileLoader;
import org.italiangrid.voms.clients.util.VersionProvider;

import eu.emi.security.authn.x509.impl.CertificateUtils;

/**
 * AbstractCLI provides a base class for command-line interface (CLI) tools.
 * It handles command-line parsing, option initialization, and logging.
 *
 * <p>This class provides functionality such as:</p>
 * <ul>
 *   <li>Parsing command-line arguments and configuration files</li>
 *   <li>Handling verbosity levels</li>
 *   <li>Displaying help and version information</li>
 * </ul>
 *
 * @author Istituto Nazionale di Fisica Nucleare
 * @since 2006
 */
public abstract class AbstractCLI {

  static {
    // Configure the security provider for certificate handling
    CertificateUtils.configureSecProvider();
  }

  /** Default temporary file path **/
  public static final String DEFAULT_TMP_PATH = "/tmp";

  /** CLI options container **/
  protected Options cliOptions;

  /** Parser for command-line options **/
  protected CommandLineParser cliParser = new DefaultParser();

  /** Parsed command-line arguments **/
  protected CommandLine commandLine = null;

  /** Name of the command **/
  protected String commandName;

  /** Logger for displaying messages **/
  protected MessageLogger logger;

  /** Flag indicating quiet mode **/
  boolean isQuiet = false;

  /** Flag indicating verbose mode **/
  boolean isVerbose = false;

  /**
   * Constructs an AbstractCLI instance with the specified command name.
   *
   * @param commandName the name of the command
   */
  protected AbstractCLI(String commandName) {
    this.commandName = commandName;
  }

  /**
   * Displays the usage information for the command.
   */
  protected final void displayUsage() {
    int lineWidth = 120;
    String header = "options:";
    String footer = "";
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp(lineWidth, commandName + " [options]", header,
      cliOptions, footer);
  }

  /**
   * Parses options from the command-line arguments.
   *
   * @param args the command-line arguments
   */
  protected final void parseOptionsFromCommandLine(String[] args) {
    try {
      commandLine = cliParser.parse(cliOptions, args);
      if (commandLineHasOption(CommonOptions.CONF)) {
        parseOptionsFromFile(getOptionValue(CommonOptions.CONF));
      }
      setVerbosityFromCommandLine();
      displayVersionIfRequested();
      displayHelpIfRequested();
    } catch (ParseException e) {
      System.err.println("Error parsing command line arguments: " + e.getMessage());
      displayUsage();
      System.exit(1);
    }
  }

  /**
   * Parses options from a file and merges them with command-line arguments.
   *
   * @param optionFileName the options file
   * @throws ParseException if an error occurs while parsing options
   */
  private void parseOptionsFromFile(String optionFileName) throws ParseException {
    List<String> options = OptionsFileLoader.loadOptions(optionFileName);
    options.addAll(commandLine.getArgList());
    commandLine = cliParser.parse(cliOptions, options.toArray(new String[0]));
  }

  /**
   * Displays version information.
   */
  protected final void displayVersion() {
    VersionProvider.displayVersionInfo(commandName);
  }

  /**
   * Initializes the available CLI options.
   *
   * @param options a list of CLI options
   */
  protected final void initOptions(List<CLIOption> options) {
    cliOptions = new Options();
    for (CLIOption o : options) {
      cliOptions.addOption(o.getOption());
    }
  }

  /**
   * Checks if a command-line option is present.
   *
   * @param option the CLI option
   * @return true if the option is present, false otherwise
   */
  protected final boolean commandLineHasOption(CLIOption option) {
    return commandLine.hasOption(option.getLongOptionName());
  }

  /**
   * Displays help information if requested.
   */
  protected final void displayHelpIfRequested() {
    if (commandLineHasOption(CommonOptions.HELP) || commandLineHasOption(CommonOptions.USAGE)) {
      displayUsage();
      System.exit(0);
    }
  }

  /**
   * Displays version information if requested.
   */
  protected final void displayVersionIfRequested() {
    if (commandLineHasOption(CommonOptions.VERSION)) {
      displayVersion();
      System.exit(0);
    }
  }

  /**
   * Retrieves the value of a given command-line option.
   *
   * @param option the CLI option
   * @return the option value, or null if not present
   */
  protected final String getOptionValue(CLIOption option) {
    if (commandLineHasOption(option)) {
      return commandLine.getOptionValue(option.getLongOptionName());
    }
    return null;
  }

  /**
   * Retrieves a list of values for a given command-line option.
   *
   * @param option the CLI option
   * @return a list of option values, or null if not present
   */
  protected final List<String> getOptionValues(CLIOption option) {
    if (commandLineHasOption(option)) {
      String[] values = commandLine.getOptionValues(option.getLongOptionName());
      return Arrays.asList(values);
    }
    return null;
  }

  /**
   * Sets verbosity levels based on command-line options.
   */
  protected final void setVerbosityFromCommandLine() {
    if (commandLineHasOption(CommonOptions.DEBUG)) {
      isVerbose = true;
    }
    if (commandLineHasOption(ProxyInitOptions.QUIET_MODE)) {
      isQuiet = true;
    }
    if (isVerbose && isQuiet) {
      throw new VOMSError("Command cannot be both verbose and quiet at the same time!");
    }
    if (isVerbose) {
      logger = new MessageLogger(MessageLogger.VERBOSE);
    } else if (isQuiet) {
      logger = new MessageLogger(MessageLogger.QUIET);
    } else {
      logger = new MessageLogger();
    }
  }

  /**
   * Executes the CLI command. This method must be implemented by subclasses.
   */
  protected abstract void execute();
}
