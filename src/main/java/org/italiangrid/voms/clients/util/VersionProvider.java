/**
 * 
 */
package org.italiangrid.voms.clients.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Util class for displaying version information.
 * 
 * @author valerioventuri
 *
 */
public class VersionProvider {

  /**
   * Display version information.
   * 
   * @param command
   */
  public static void displayVersionInfo(String command) {
    
    Properties properties = new Properties();
    
    InputStream inputStream = VersionProvider.class.getClassLoader().getResourceAsStream("version.properties");
    
    try {
      
      properties.load(inputStream);
    
    } catch (IOException e) {

      throw new RuntimeException(e);
    }
    
    String version = properties.getProperty("version");
    
    System.out.println(command);
    System.out.println("Version: " + version);
    
  }
  
}
