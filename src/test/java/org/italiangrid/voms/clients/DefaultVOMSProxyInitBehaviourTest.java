// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.italiangrid.voms.clients.impl.DefaultVOMSProxyInitBehaviour;
import org.italiangrid.voms.clients.impl.InitListenerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import eu.emi.security.authn.x509.proxy.ProxyCertificate;
import eu.emi.security.authn.x509.proxy.ProxyType;

class DefaultVOMSProxyInitBehaviourTest {

  private static final int LIFETIME_SECONDS = 3600; // 1 hour
  private static final int BACKDATE_SECONDS = 300; // 5 minutes

  private static final String LIFETIME_WARNING =
      "proxy lifetime limited to issuing credential lifetime.";

  @TempDir
  Path directory;

  private KeyPair issuerKey;
  private ProxyInitParams params;
  private InitListenerAdapter listener;
  private DefaultVOMSProxyInitBehaviour behaviour;

  private X509Certificate generatedCertificate;
  private final List<String> warnings = new ArrayList<>();

  @BeforeEach
  void setUp() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    issuerKey = generator.generateKeyPair();

    params = new ProxyInitParams();
    params.setCertFile(directory.resolve("issuer.pem").toString());
    params.setKeyFile(directory.resolve("issuer-key.pem").toString());
    params.setGeneratedProxyFile(directory.resolve("proxy.pem").toString());
    params.setProxyLifetimeInSeconds(LIFETIME_SECONDS);
    params.setProxyType(ProxyType.RFC3820);
    params.setKeySize(2048);
    params.setValidateUserCredential(false);
    params.setVomsCommands(Collections.emptyList());
    params.setEnforcingChainIntegrity(true);

    listener = mock(InitListenerAdapter.class);

    doAnswer(invocation -> {
      Throwable cause = invocation.getArgument(0);
      throw new AssertionError("Failed to load test credentials", cause);
    }).when(listener).notifyLoadCredentialFailure(any(Throwable.class), any(String[].class));

    doAnswer(invocation -> {
      ProxyCertificate proxy = invocation.getArgument(1);
      generatedCertificate = proxy.getCredential().getCertificate();

      List<String> reportedWarnings = invocation.getArgument(2);
      warnings.addAll(reportedWarnings);
      return null;
    }).when(listener).proxyCreated(anyString(), any(ProxyCertificate.class), anyList());

    behaviour = new DefaultVOMSProxyInitBehaviour(null, listener);
  }

  /**
   * Verifies that the generated proxy starts five minutes before the current time and expires one
   * requested lifetime after the current time when the issuing credential does not constrain its
   * validity.
   *
   * <p>
   * The total validity interval therefore includes the requested lifetime plus the five-minute
   * backdating allowance. No creation warnings are expected.
   *
   * @throws Exception if test credential creation or proxy generation fails
   */
  @Test
  void backdatesNotBeforeWithoutReducingRemainingLifetime() throws Exception {
    writeIssuer(Instant.now().plus(2, ChronoUnit.DAYS));

    Instant before = Instant.now();
    behaviour.initProxy(params);
    Instant after = Instant.now();

    assertBackdatedStart(before, after);
    assertWithin(generatedCertificate.getNotAfter(), before.plusSeconds(LIFETIME_SECONDS),
        after.plusSeconds(LIFETIME_SECONDS));

    assertEquals(LIFETIME_SECONDS + BACKDATE_SECONDS, (generatedCertificate.getNotAfter().getTime()
        - generatedCertificate.getNotBefore().getTime()) / 1000);

    assertTrue(warnings.isEmpty());
  }

  /**
   * Verifies that the proxy expiry is limited to the issuing credential's expiry when it precedes
   * the requested proxy expiry and chain integrity checks are enabled.
   *
   * <p>
   * The five-minute backdating of the proxy start time is preserved, and a warning reports that the
   * proxy lifetime was limited.
   *
   * @throws Exception if test credential creation or proxy generation fails
   */
  @Test
  void limitsExpiryToIssuerExpiryAndPreservesBackdatedStart() throws Exception {
    Instant issuerExpiry = Instant.now().plusSeconds(1200).truncatedTo(ChronoUnit.SECONDS);
    writeIssuer(issuerExpiry);

    Instant before = Instant.now();
    behaviour.initProxy(params);
    Instant after = Instant.now();

    assertBackdatedStart(before, after);
    assertEquals(Date.from(issuerExpiry), generatedCertificate.getNotAfter());
    assertEquals(Collections.singletonList(LIFETIME_WARNING), warnings);
  }

  /**
   * Verifies that disabling chain integrity checks does not disable backdating: the generated proxy
   * still starts five minutes before the current time and expires one requested lifetime after the
   * current time.
   *
   * <p>
   * The issuing credential outlives the requested proxy, and no creation warnings are expected.
   *
   * @throws Exception if test credential creation or proxy generation fails
   */
  @Test
  void backdatesNotBeforeWhenChainIntegrityChecksAreDisabled() throws Exception {
    writeIssuer(Instant.now().plus(2, ChronoUnit.DAYS));
    params.setEnforcingChainIntegrity(false);

    Instant before = Instant.now();
    behaviour.initProxy(params);
    Instant after = Instant.now();

    assertBackdatedStart(before, after);
    assertWithin(generatedCertificate.getNotAfter(), before.plusSeconds(LIFETIME_SECONDS),
        after.plusSeconds(LIFETIME_SECONDS));

    assertTrue(warnings.isEmpty());
  }

  private void assertBackdatedStart(Instant before, Instant after) {
    assertNotNull(generatedCertificate);
    assertWithin(generatedCertificate.getNotBefore(), before.minusSeconds(BACKDATE_SECONDS),
        after.minusSeconds(BACKDATE_SECONDS));
  }

  private static void assertWithin(Date actual, Instant earliest, Instant latest) {
    // X.509 certificate timestamps have second precision.
    Instant value = actual.toInstant();
    Instant lower = earliest.truncatedTo(ChronoUnit.SECONDS);
    Instant upper = latest.truncatedTo(ChronoUnit.SECONDS);

    assertFalse(value.isBefore(lower), () -> value + " is before " + lower);
    assertFalse(value.isAfter(upper), () -> value + " is after " + upper);
  }

  private void writeIssuer(Instant expiresAt) throws Exception {
    X500Name subject = new X500Name("CN=Test User,O=VOMS Tests");

    JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(subject, BigInteger.ONE,
        Date.from(Instant.now().minus(1, ChronoUnit.DAYS)), Date.from(expiresAt), subject,
        issuerKey.getPublic());

    X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(
        builder.build(new JcaContentSignerBuilder("SHA256withRSA").build(issuerKey.getPrivate())));

    writePem(directory.resolve("issuer.pem"), certificate);
    writePem(directory.resolve("issuer-key.pem"), issuerKey.getPrivate());

    Path keyPath = directory.resolve("issuer-key.pem");
    writePem(keyPath, issuerKey.getPrivate());
    Files.setPosixFilePermissions(keyPath, PosixFilePermissions.fromString("rw-------"));
  }

  private static void writePem(Path path, Object object) throws Exception {
    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.US_ASCII);
        JcaPEMWriter pem = new JcaPEMWriter(writer)) {
      pem.writeObject(object);
    }
  }
}
