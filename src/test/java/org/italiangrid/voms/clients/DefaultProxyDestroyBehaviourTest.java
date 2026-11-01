// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.italiangrid.voms.clients.impl.DefaultProxyDestroyBehaviour;
import org.italiangrid.voms.clients.impl.ProxyDestroyListenerAdapter;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DefaultProxyDestroyBehaviourTest {

  @TempDir
  Path directory;

  @Test
  void deletesProxyAtExplicitPath() throws IOException {

    Path proxy = Files.createFile(directory.resolve("proxy.pem"));

    ProxyDestroyParams params = new ProxyDestroyParams();
    params.setProxyFile(proxy.toString());

    new DefaultProxyDestroyBehaviour(mock(ProxyDestroyListenerAdapter.class)).destroyProxy(params);

    assertFalse(Files.exists(proxy));
  }

  @Test
  public void test() throws IOException {

    /*
     * Make sure a file exists in /tmp/x509_u<uid>.
     * It does not have to actually contains a proxy certificate as destroy delete 
     * it anyway.
     */

    File file = new File(VOMSProxyPathBuilder.buildProxyPath());
    file.createNewFile();

    new DefaultProxyDestroyBehaviour(null).destroyProxy(new ProxyDestroyParams());

    assertFalse(file.exists());
  }

  /**
   * Tests that destroyProxy called passing an alternative proxy location in the parameters, delete
   * the proxy certificate file.
   * 
   * @throws IOException when it can not create the file to be destroyed
   */
  @Test
  public void anotherTest() throws IOException {

    /*
     * create the file that destroys is going to delete. It does not have to actually contains a
     * proxy certificate as destroy delete it anyway.
     */

    String fileName = "/tmp/destroy_me";

    File file = new File(fileName);
    file.createNewFile();

    ProxyDestroyParams params = new ProxyDestroyParams();
    params.setProxyFile(fileName);

    new DefaultProxyDestroyBehaviour(null).destroyProxy(params);

    assertFalse(file.exists());
  }

}
