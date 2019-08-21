/*
 * Copyright (c) 2017 Google Inc.
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

import com.google.api.server.spi.tools.GetOpenApiDocAction;
import com.google.common.base.Strings;
import java.io.File;
import java.util.List;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/** Goal which generates openapi docs. */
@Mojo(
    name = "openApiDocs",
    requiresDependencyResolution = ResolutionScope.COMPILE,
    defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class OpenApiDocsMojo extends AbstractEndpointsWebAppMojo {

  /** Output directory for openapi docs. */
  @Parameter(
      defaultValue = "${project.build.directory}/openapi-docs",
      property = "endpoints.openApiDocDir",
      required = true)
  private File openApiDocDir;

  /** API title in the Open API specification. */
  @Parameter(property = "endpoints.title", required = false)
  private String title;

  /** API description of the Open API specification. */
  @Parameter(property = "endpoints.description", required = false)
  private String description;

  /** Adds the Google JSON error model as default response. */
  @Parameter(property = "endpoints.addGoogleJsonErrorAsDefaultResponse", required = false)
  private boolean addGoogleJsonErrorAsDefaultResponse;

  /** Adds Google JSON error models as non-2xx response codes. */
  @Parameter(property = "endpoints.addErrorCodesForServiceExceptions", required = false)
  private boolean addErrorCodesForServiceExceptions;

  /** Extracts common parameters to refs at specification level. */
  @Parameter(property = "endpoints.extractCommonParametersAsRefs", required = false)
  private boolean extractCommonParametersAsRefs;

  /** Combine common parameters in the same path. */
  @Parameter(property = "endpoints.combineCommonParametersInSamePath", required = false)
  private boolean combineCommonParametersInSamePath;

  @Override
  protected String getActionName() {
    return GetOpenApiDocAction.NAME;
  }

  @Override
  protected File getOutputDirectory() {
    return openApiDocDir;
  }

  @Override
  protected String getOutputPath() {
    return new File(openApiDocDir, "openapi.json").getAbsolutePath();
  }

  @Override
  protected void addSpecificParameters(List<String> params) {
    if (!Strings.isNullOrEmpty(title)) {
      params.add("-t");
      params.add(title);
    }
    if (!Strings.isNullOrEmpty(description)) {
      params.add("-d");
      params.add(description);
    }
    if (addGoogleJsonErrorAsDefaultResponse) {
      params.add("--addGoogleJsonErrorAsDefaultResponse");
    }
    if (addErrorCodesForServiceExceptions) {
      params.add("--addErrorCodesForServiceExceptions");
    }
    if (extractCommonParametersAsRefs) {
      params.add("--extractCommonParametersAsRefs");
    }
    if (combineCommonParametersInSamePath) {
      params.add("--combineCommonParametersInSamePath");
    }
  }
}
