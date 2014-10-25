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
package org.italiangrid.voms.clients;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.italiangrid.voms.clients.impl.DefaultProxyDestroyBehaviour;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.junit.Test;

public class DefaultProxyDestroyBehaviourTest {

  /**
   * The object under test.
   */
  DefaultProxyDestroyBehaviour defaultProxyDestroyBehaviour =
      new DefaultProxyDestroyBehaviour(null);
  
  /**
   * Tests that destroyProxy called with an empty arguments delete
   * the proxy certificate file.
   * 
   * @throws IOException when it can not create the file to be destroyed
   */
  @Test
  public void test() throws IOException {
    
    DefaultProxyDestroyBehaviour behaviour = 
        new DefaultProxyDestroyBehaviour(null);
    
    /*
     * make sure a file exists in /tmp/x509_u<uid>. It does not have
     * to actually contains a proxy certificate as destroy delete 
     * it anyway.
     * 
     */
    
    File file = new File(VOMSProxyPathBuilder.buildProxyPath());
    file.createNewFile();
        
    ProxyDestroyParams params = new ProxyDestroyParams();
    
    behaviour.destroyProxy(params);

    /*
     * check that /tmp/x509_u<uid> exists no longer.
     */
    
    assertFalse(file.exists());
  }
 
  /**
   * Tests that destroyProxy called passing an alternative
   * proxy location in the parameters, delete the proxy certificate file.
   * 
   * @throws IOException when it can not create the file to be destroyed
   */
  @Test
  public void anotherTest() throws IOException {
    
    DefaultProxyDestroyBehaviour behaviour = 
        new DefaultProxyDestroyBehaviour(null);
    
    /*
     * create the file that destroys is going to delete. It does not have
     * to actually contains a proxy certificate as destroy delete it
     * anyway.
     * 
     */
    
    String fileName = "/tmp/destroy_me";
    
    File file = new File(fileName);
    file.createNewFile();
        
    ProxyDestroyParams params = new ProxyDestroyParams();
    params.setProxyFile(fileName);
    
    behaviour.destroyProxy(params);

    /*
     * check that /tmp/x509_u<uid> exists no longer.
     */
    
    assertFalse(file.exists());
  }
 
}
