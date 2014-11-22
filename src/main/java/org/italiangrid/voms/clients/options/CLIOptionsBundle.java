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
package org.italiangrid.voms.clients.options;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.italiangrid.voms.clients.options.VOMSCLIOptionBuilder.BundleKey;

public enum CLIOptionsBundle {
  common, proxyInit, proxyInfo, proxyDestroy;

  private CLIOptionsBundle() {

    String bundleName = VOMSCLIOptionBuilder.class.getPackage().getName() + "."
      + this.name() + "Options";
    bundle = ResourceBundle.getBundle(bundleName);
    if (bundle == null)
      throw new IllegalStateException(
        "Cannot load VOMS CLI options: options bundle not found: " + bundleName);
  }

  private ResourceBundle bundle;

  public ResourceBundle getBundle() {

    return bundle;
  }

  public String getStringFromBundle(String longOpt, BundleKey key) {

    String returnValue = null;

    try {

      returnValue = getBundle().getString(longOpt + "." + key.name());

    } catch (MissingResourceException e) {
      // Swallow exception
    }

    return returnValue;
  }
}