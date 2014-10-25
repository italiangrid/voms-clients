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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;

/**
 * Util class for loading options from a file.
 * 
 * @author valerioventuri
 * 
 */
public class OptionsFileLoader {

  /**
   * Load options from a file
   * 
   * @param optionFileName
   *          the file containing the options
   * @return a list of options
   */
  public static List<String> loadOptions(String optionFileName) {

    List<String> args = new ArrayList<String>();

    File optionFile = new File(optionFileName);

    StringWriter stringWriter = new StringWriter();

    try {

      InputStream inputStream = new FileInputStream(optionFile);

      IOUtils.copy(inputStream, stringWriter);

    } catch (FileNotFoundException e) {

      System.err.println("Error reading options file: " + e.getMessage());
      System.exit(1);

    } catch (IOException e) {

      System.err.println("Error reading options file: " + e.getMessage());
      System.exit(1);
    }

    String string = stringWriter.toString();

    StringTokenizer stringTokenizer = new StringTokenizer(string);

    while (stringTokenizer.hasMoreTokens()) {

      args.add(stringTokenizer.nextToken());
    }

    return args;
  }

}
