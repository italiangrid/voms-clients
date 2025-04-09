// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.italiangrid.voms.clients.impl.DefaultVOMSCommandsParser;
import org.junit.Assert;
import org.junit.Test;

public class TestVOMSCommandsParser {

  @Test
  public void testEmpyCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Collections
      .<String> emptyList());

    Assert.assertTrue(map.isEmpty());
  }

  @Test
  public void testNullCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(null);

    Assert.assertNull(map);

  }

  @Test
  public void testSingleVOCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser
      .parseCommands(Arrays.asList("atlas"));

    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(map.size() == 1);
    Assert.assertNotNull(map.get("atlas"));
    Assert.assertTrue(map.get("atlas").isEmpty());
  }

  @Test
  public void testSingleFQANCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays
      .asList("atlas:/atlas/Role=production"));

    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(map.size() == 1);
    Assert.assertNotNull(map.get("atlas"));
    Assert.assertFalse(map.get("atlas").isEmpty());
    Assert.assertTrue(map.get("atlas").size() == 1);
    Assert.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
  }

  @Test
  public void testMultipleFQANsCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays.asList(
      "atlas:/atlas/Role=production", "atlas:/atlas/Role=admin"));

    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(map.size() == 1);
    Assert.assertNotNull(map.get("atlas"));
    Assert.assertFalse(map.get("atlas").isEmpty());
    Assert.assertTrue(map.get("atlas").size() == 2);
    Assert.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
    Assert.assertEquals("/atlas/Role=admin", map.get("atlas").get(1));
  }

  @Test
  public void testMultipleFQANsWithRepeatedVOCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays.asList(
      "atlas:/atlas/Role=production", "atlas:/atlas/Role=admin", "atlas"));

    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(map.size() == 1);
    Assert.assertNotNull(map.get("atlas"));
    Assert.assertFalse(map.get("atlas").isEmpty());
    Assert.assertTrue(map.get("atlas").size() == 2);
    Assert.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
    Assert.assertEquals("/atlas/Role=admin", map.get("atlas").get(1));
  }

  @Test
  public void testMultipleVOsCommandList() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();

    Map<String, List<String>> map = parser.parseCommands(Arrays.asList(
      "atlas:/atlas/Role=production", "atlas:/atlas/Role=admin", "cms",
      "cms:/cms/camaghe"));
    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(map.size() == 2);
    Assert.assertNotNull(map.get("atlas"));
    Assert.assertFalse(map.get("atlas").isEmpty());
    Assert.assertNotNull(map.get("cms"));
    Assert.assertFalse(map.get("cms").isEmpty());
    Assert.assertTrue(map.get("atlas").size() == 2);
    Assert.assertEquals("/atlas/Role=production", map.get("atlas").get(0));
    Assert.assertEquals("/atlas/Role=admin", map.get("atlas").get(1));
    Assert.assertTrue(map.get("cms").size() == 1);
    Assert.assertEquals("/cms/camaghe", map.get("cms").get(0));
  }

  @Test
  public void testSupportForLegacyAllCommand() {

    DefaultVOMSCommandsParser parser = new DefaultVOMSCommandsParser();
    Map<String, List<String>> map = parser.parseCommands(Arrays
      .asList("atlas:all"));

    Assert.assertNotNull(map);
    Assert.assertFalse(map.isEmpty());
    Assert.assertTrue(map.size() == 1);
    Assert.assertNotNull(map.get("atlas"));
    Assert.assertTrue(map.get("atlas").isEmpty());
  }
}
