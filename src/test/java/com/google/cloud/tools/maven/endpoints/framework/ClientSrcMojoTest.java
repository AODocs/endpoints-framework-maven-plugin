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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ClientSrcMojoTest {

  private static final String GEN_DIR_DEFAULT = "target/generated-sources/endpoints";
  private static final String GEN_DIR_MODIFIED = "src-gen";
  private static final String GENERATED_FILE_PATH = "/com/example/testApi/v1/TestApi.java";

  @Rule public TemporaryFolder tmpDir = new TemporaryFolder();

  private void buildAndVerify(File projectDir, String genDir) throws VerificationException {
    Verifier verifier = new Verifier(projectDir.getAbsolutePath());
    verifier.executeGoals(Arrays.asList("compile", "endpoints-framework:clientSrc"));
    verifier.verifyErrorFreeLog();
    verifier.assertFilePresent(genDir + GENERATED_FILE_PATH);
  }

  @Test
  public void testDefault() throws XmlPullParserException, IOException, VerificationException {
    File testDir = new TestProject(tmpDir.getRoot(), "/projects/server").build();

    buildAndVerify(testDir, GEN_DIR_DEFAULT);
  }

  @Test
  public void testGeneratedSrcDir()
      throws XmlPullParserException, IOException, VerificationException {
    File testDir =
        new TestProject(tmpDir.getRoot(), "/projects/server")
            .configuration(
                "<configuration><generatedSrcDir>"
                    + GEN_DIR_MODIFIED
                    + "</generatedSrcDir></configuration>")
            .build();

    buildAndVerify(testDir, GEN_DIR_MODIFIED);
  }
}
