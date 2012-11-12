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
   * Load options from a file, adding them to an array of arguments.
   * 
   * @param oldArgs
   *          the arguments array
   * @param optionFileName
   *          the file containing the options
   * @return an arguments array containing also the new options
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
