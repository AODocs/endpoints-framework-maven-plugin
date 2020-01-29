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

import com.google.api.server.spi.tools.EndpointsTool;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractEndpointsWebAppMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true)
  MavenProject project;

  /** Location of compile java classes. */
  @Parameter(defaultValue = "${project.build.outputDirectory}", readonly = true)
  private File classesDir;

  /** Default hostname of the Endpoint Host. */
  @Parameter(property = "endpoints.hostname", required = false)
  private String hostname;

  /** Default basepath of the Endpoint Host. */
  @Parameter(property = "endpoints.basepath", required = false)
  private String basePath;

  /** List of names of service classes (endpoints classes). */
  @Parameter(property = "endpoints.serviceClasses", required = false)
  private List<String> serviceClasses;

  /** Location of java web application "webapp" directory. */
  @Parameter(
      defaultValue = "${basedir}/src/main/webapp",
      property = "endpoints.webappDir",
      required = true)
  private File webappDir;

  @Override
  public final void execute() throws MojoExecutionException {
    preExecute();
    File outputDirectory = getOutputDirectory();
    if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
      throw new MojoExecutionException(
          "Failed to create output directory: " + outputDirectory.getAbsolutePath());
    }
    try {
      List<String> params = createParameterList(getActionName());
      addSpecificParameters(params);
      if (serviceClasses != null) {
        params.addAll(serviceClasses);
      }
      getLog().info("Endpoints Tool params : " + params.toString());
      new EndpointsTool().execute(params.toArray(new String[0]));
    } catch (Exception e) {
      throw new MojoExecutionException("Endpoints Tool Error", e);
    }
  }

  private List<String> createParameterList(String actionName)
      throws DependencyResolutionRequiredException {
    String classpath = Joiner.on(File.pathSeparator).join(project.getRuntimeClasspathElements());
    classpath += File.pathSeparator + classesDir;
    List<String> params =
        new ArrayList<>(
            Arrays.asList(
                actionName,
                "-o",
                getOutputPath(),
                "-cp",
                classpath,
                "-w",
                webappDir.getAbsolutePath()));
    if (!Strings.isNullOrEmpty(hostname)) {
      params.add("-h");
      params.add(hostname);
    }
    if (!Strings.isNullOrEmpty(basePath)) {
      params.add("-p");
      params.add(basePath);
    }
    String additionalParameters = getAdditionalParameters();
    if (!Strings.isNullOrEmpty(additionalParameters)) {
      params.addAll(Splitter.on(' ').trimResults().splitToList(additionalParameters));
    }

    return params;
  }

  protected void preExecute() throws MojoExecutionException {}

  protected abstract String getActionName();

  protected abstract File getOutputDirectory();

  protected String getOutputPath() {
    return getOutputDirectory().getAbsolutePath();
  }

  protected abstract String getAdditionalParameters();

  protected abstract void addSpecificParameters(List<String> params);
}
