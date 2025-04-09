// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.italiangrid.voms.request.impl.DefaultVOMSESLookupStrategy;

public class CustomVOMSESLookupStrategy extends DefaultVOMSESLookupStrategy {

  String customVOMSESPath;

  public CustomVOMSESLookupStrategy(String vomsesPath) {

    this.customVOMSESPath = vomsesPath;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.italiangrid.voms.request.impl.BaseVOMSESLookupStrategy#lookupVomsesInfo
   * ()
   */
  @Override
  public List<File> lookupVomsesInfo() {

    List<String> searchedPaths = searchedPaths();
    List<File> vomsesPaths = new ArrayList<File>();

    for (String p : searchedPaths) {
      File f = new File(p);
      if (f.exists())
        vomsesPaths.add(f);
    }

    return vomsesPaths;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.italiangrid.voms.request.impl.BaseVOMSESLookupStrategy#searchedPaths()
   */
  @Override
  public List<String> searchedPaths() {

    List<String> searchedPaths = super.searchedPaths();
    searchedPaths.add(customVOMSESPath);
    return searchedPaths;
  }

}
