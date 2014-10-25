/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
