// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.italiangrid.voms.clients.impl.DefaultVOMSCommandsParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVOMSCommandsParser {

  @Test
  public void testEmpyCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Collections
      .<String> emptyList());

    Assertions.assertTrue(map.isEmpty());
  }

  @Test
  public void testNullCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(null);

    Assertions.assertNull(map);

  }

  @Test
  public void testSingleVOCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser
      .parseCommands(Arrays.asList("atlas"));

    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertTrue(map.size() == 1);
    Assertions.assertNotNull(map.get("atlas"));
    Assertions.assertTrue(map.get("atlas").isEmpty());
  }

  @Test
  public void testSingleFQANCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays
      .asList("atlas:/atlas/Role=production"));

    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertTrue(map.size() == 1);
    Assertions.assertNotNull(map.get("atlas"));
    Assertions.assertFalse(map.get("atlas").isEmpty());
    Assertions.assertTrue(map.get("atlas").size() == 1);
    Assertions.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
  }

  @Test
  public void testMultipleFQANsCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays.asList(
      "atlas:/atlas/Role=production", "atlas:/atlas/Role=admin"));

    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertTrue(map.size() == 1);
    Assertions.assertNotNull(map.get("atlas"));
    Assertions.assertFalse(map.get("atlas").isEmpty());
    Assertions.assertTrue(map.get("atlas").size() == 2);
    Assertions.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
    Assertions.assertEquals("/atlas/Role=admin", map.get("atlas").get(1));
  }

  @Test
  public void testMultipleFQANsWithRepeatedVOCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays.asList(
      "atlas:/atlas/Role=production", "atlas:/atlas/Role=admin", "atlas"));

    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertTrue(map.size() == 1);
    Assertions.assertNotNull(map.get("atlas"));
    Assertions.assertFalse(map.get("atlas").isEmpty());
    Assertions.assertTrue(map.get("atlas").size() == 2);
    Assertions.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
    Assertions.assertEquals("/atlas/Role=admin", map.get("atlas").get(1));
  }

  @Test
  public void testMultipleVOsCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays.asList(
      "atlas:/atlas/Role=production", "atlas:/atlas/Role=admin", "cms",
      "cms:/cms/camaghe"));
    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertTrue(map.size() == 2);
    Assertions.assertNotNull(map.get("atlas"));
    Assertions.assertFalse(map.get("atlas").isEmpty());
    Assertions.assertNotNull(map.get("cms"));
    Assertions.assertFalse(map.get("cms").isEmpty());
    Assertions.assertTrue(map.get("atlas").size() == 2);
    Assertions.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
    Assertions.assertEquals("/atlas/Role=admin", map.get("atlas").get(1));
    Assertions.assertTrue(map.get("cms").size() == 1);
    Assertions.assertEquals("/cms/camaghe", map.get("cms").get(0));
  }

  @Test
  public void testSupportForLegacyAllCommand() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();
    Map<String, List<String>> map = parser.parseCommands(Arrays
      .asList("atlas:all"));

    Assertions.assertNotNull(map);
    Assertions.assertFalse(map.isEmpty());
    Assertions.assertTrue(map.size() == 1);
    Assertions.assertNotNull(map.get("atlas"));
    Assertions.assertTrue(map.get("atlas").isEmpty());
  }
}
