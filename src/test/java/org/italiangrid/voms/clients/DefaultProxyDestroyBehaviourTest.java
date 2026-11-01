// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.impl.DefaultProxyDestroyBehaviour;
import org.italiangrid.voms.clients.impl.ProxyDestroyListenerAdapter;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

class DefaultProxyDestroyBehaviourTest {

  @TempDir
  Path directory;

  @Test
  void deletesProxyAtDefaultPath() throws IOException {

    // Environment lookup takes precedence over the default path in production.
    assumeTrue(System.getenv("X509_USER_PROXY") == null,
        "The default-path branch requires X509_USER_PROXY to be unset");

    Path proxy = Files.createFile(directory.resolve("default-proxy.pem"));
    destroyProxy(new ProxyDestroyParams());
    assertFalse(Files.exists(proxy));
  }

  @Test
  void deletesProxyAtExplicitPathWithoutDeletingDefaultProxy() throws IOException {

    Path defaultProxy = Files.createFile(directory.resolve("default-proxy.pem"));
    Path explicitProxy = Files.createFile(directory.resolve("explicit-proxy.pem"));
    ProxyDestroyParams params = paramsFor(explicitProxy);
    destroyProxy(params);
    assertFalse(Files.exists(explicitProxy));
    assertTrue(Files.exists(defaultProxy));
  }

  @Test
  void reportsDeletionFailureForNonEmptyDirectory() throws IOException {
    // A non-empty directory makes deletion fail even when tests run as root.
    // This exercises a real filesystem failure without mocking File.delete().
    Path target = Files.createDirectory(directory.resolve("proxy-directory"));
    Path child = target.resolve("existing-data");
    byte[] original = "must remain unchanged".getBytes(StandardCharsets.US_ASCII);
    Files.write(child, original);
    ProxyDestroyParams params = paramsFor(target);

    assertThrows(VOMSError.class, () -> destroyProxy(params),
        "A failed deletion must not be reported as success");
    assertTrue(Files.isDirectory(target));
    assertArrayEquals(original, Files.readAllBytes(child));
  }

  @Test
  void reportsDeletionFailureWhenParentDirectoryIsNotWritable() throws IOException {
    assumeTrue(Files.getFileStore(directory).supportsFileAttributeView("posix"),
        "This test requires POSIX directory permissions");

    Path parent = Files.createDirectory(directory.resolve("read-only-parent"));
    Path proxy = parent.resolve("proxy.pem");
    byte[] original = "existing proxy".getBytes(StandardCharsets.US_ASCII);
    Files.write(proxy, original);
    ProxyDestroyParams params = paramsFor(proxy);
    Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(parent);

    try {
      Files.setPosixFilePermissions(parent, PosixFilePermissions.fromString("r-x------"));
      assumeFalse(parent.toFile().canWrite(),
          "Privileged users may bypass directory write permissions");

      assertAll(
          () -> assertThrows(VOMSError.class, () -> destroyProxy(params),
              "A failed deletion must not be reported as success"),
          () -> assertArrayEquals(original, Files.readAllBytes(proxy)));
    } finally {
      // Restore access even when the test is skipped or an assertion fails.
      Files.setPosixFilePermissions(parent, permissions);
    }
  }

  private ProxyDestroyParams paramsFor(Path path) {

    ProxyDestroyParams params = new ProxyDestroyParams();
    params.setProxyFile(path.toString());
    return params;
  }

  private void destroyProxy(ProxyDestroyParams params) {

    // All paths belong to @TempDir; never resolve the user's real proxy path.
    try (MockedStatic<VOMSProxyPathBuilder> paths = mockStatic(VOMSProxyPathBuilder.class)) {
      paths.when(VOMSProxyPathBuilder::buildProxyPath)
        .thenReturn(directory.resolve("default-proxy.pem").toString());

      new DefaultProxyDestroyBehaviour(mock(ProxyDestroyListenerAdapter.class))
        .destroyProxy(params);
    }
  }
}
