/*
 * Copyright (c) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.google.cloud.tools.maven.endpoints.framework;

import com.google.api.server.spi.tools.GetClientLibAction;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/** Maven goal to generate client libraries (as zips). */
@Mojo(
    name = "clientLibs",
    requiresDependencyResolution = ResolutionScope.COMPILE,
    defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class ClientLibsMojo extends AbstractEndpointsWebAppMojo {

  /** Output directory for client libraries. */
  @Parameter(
      defaultValue = "${project.build.directory}/client-libs",
      property = "endpoints.clientLibDir",
      required = true)
  private File clientLibDir;

  @Override
  protected String getActionName() {
    return GetClientLibAction.NAME;
  }

  @Override
  protected File getOutputDirectory() {
    return clientLibDir;
  }

  @Override
  protected void addSpecificParameters(List<String> params) {
    params.addAll(Arrays.asList("-l", "java", "-bs", "maven"));
  }
}
