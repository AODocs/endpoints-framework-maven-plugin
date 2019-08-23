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

  /** Additional parameters to pass to Open API generation action. */
  @Parameter(property = "endpoints.openApi.additionalParameters", required = false)
  private String openApiAdditionalParameters;

  /** Output directory for openapi docs. */
  @Parameter(
      defaultValue = "${project.build.directory}/openapi-docs/openapi.json",
      property = "endpoints.openApi.doc",
      required = true)
  private File openApiDoc;

  /** API title (used only in the Open API specification). */
  @Parameter(property = "endpoints.title", required = false)
  private String title;

  /** API description (used only in the Open API specification). */
  @Parameter(property = "endpoints.description", required = false)
  private String description;

  /** API name in Endpoints Management console (allows to reuse same host). */
  @Parameter(property = "endpoints.apiName", required = false)
  private String apiName;

  /** Adds the Google JSON error model as default response in Open API specification. */
  @Parameter(property = "endpoints.openApi.addGoogleJsonErrorAsDefaultResponse", required = false)
  private boolean openApiAddGoogleJsonErrorAsDefaultResponse;

  /** Adds Google JSON error models as non-2xx response codes in Open API specification. */
  @Parameter(property = "endpoints.openApi.addErrorCodesForServiceExceptions", required = false)
  private boolean openApiAddErrorCodesForServiceExceptions;

  /** Extracts common parameters to refs at specification level in Open API specification. */
  @Parameter(property = "endpoints.openApi.extractCommonParametersAsRefs", required = false)
  private boolean openApiExtractCommonParametersAsRefs;

  /** Combine common parameters in the same path in Open API specification. */
  @Parameter(property = "endpoints.openApi.combineCommonParametersInSamePath", required = false)
  private boolean openApiCombineCommonParametersInSamePath;

  @Override
  protected String getActionName() {
    return GetOpenApiDocAction.NAME;
  }

  @Override
  protected File getOutputDirectory() {
    return openApiDoc.getParentFile();
  }

  @Override
  protected String getOutputPath() {
    return openApiDoc.getAbsolutePath();
  }

  @Override
  protected String getAdditionalParameters() {
    return openApiAdditionalParameters;
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
    if (!Strings.isNullOrEmpty(apiName)) {
      params.add("-a");
      params.add(apiName);
    }
    if (openApiAddGoogleJsonErrorAsDefaultResponse) {
      params.add("--addGoogleJsonErrorAsDefaultResponse");
    }
    if (openApiAddErrorCodesForServiceExceptions) {
      params.add("--addErrorCodesForServiceExceptions");
    }
    if (openApiExtractCommonParametersAsRefs) {
      params.add("--extractCommonParametersAsRefs");
    }
    if (openApiCombineCommonParametersInSamePath) {
      params.add("--combineCommonParametersInSamePath");
    }
  }
}
