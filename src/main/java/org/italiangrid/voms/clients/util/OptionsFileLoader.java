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
   * @param oldArgs the arguments array
   * @param optionFileName the file containing the options
   * @return an arguments array containing also the new options
   */
  public static String[] loadOptions(String[] oldArgs, String optionFileName) {
    
    File optionFile = new File(optionFileName);
    
    StringWriter stringWriter = new StringWriter();
    
    try {

      InputStream inputStream = new FileInputStream(optionFile);
      
      IOUtils.copy(inputStream, stringWriter);
      
    } catch (FileNotFoundException e) {
    
      System.err.println(e.getMessage());
    
    } catch (IOException e) {

      System.err.println(e.getMessage());
    }

    String string = stringWriter.toString();
    
    StringTokenizer stringTokenizer = new StringTokenizer(string);
    
    List<String> otherArgs = new ArrayList<String>();
    
    while(stringTokenizer.hasMoreTokens()) {
      
      otherArgs.add(stringTokenizer.nextToken());
    }

    for(String arg : oldArgs) {
      
      otherArgs.add(arg);
    }
    
    return otherArgs.toArray(new String[0]);
  }

}
