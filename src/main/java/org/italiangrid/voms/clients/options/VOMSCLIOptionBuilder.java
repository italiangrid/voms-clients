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

import org.apache.commons.cli.Option;

public class VOMSCLIOptionBuilder {

  enum BundleKey {
    opt, description, hasArg, argDescription
  }

  public static Option buildOption(String longOpt, CLIOptionsBundle b) {

    String shortOpt = b.getStringFromBundle(longOpt, BundleKey.opt);
    String description = b.getStringFromBundle(longOpt, BundleKey.description);
    boolean hasArg = false;

    if (b.getStringFromBundle(longOpt, BundleKey.hasArg) != null)
      hasArg = Boolean.parseBoolean(b.getStringFromBundle(longOpt,
        BundleKey.hasArg));

    String argDescription = b.getStringFromBundle(longOpt,
      BundleKey.argDescription);

    Option o = new Option(shortOpt, longOpt, hasArg, description);
    o.setArgName(argDescription);
    return o;

  }

  private VOMSCLIOptionBuilder() {

  }
}
