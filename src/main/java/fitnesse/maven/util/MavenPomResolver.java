/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package fitnesse.maven.util;

import fitnesse.maven.PomFile;

import java.io.File;


public class MavenPomResolver {
    private FileUtil fileUtil;

    public MavenPomResolver() {
        this(new FileUtil());
    }

    protected MavenPomResolver(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public PomFile resolve(File pomFile) {
        if (fileUtil.exists(pomFile)) {
            if (fileUtil.isDirectory(pomFile)) {
                File pom = new File(pomFile, "pom.xml");
                return fileUtil.exists(pom) ? new PomFile(pom) : null;
            }
            return new PomFile(pomFile);
        }
        return null;
    }
}
