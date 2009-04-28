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

import fitnesse.maven.io.FileUtil;
import fitnesse.maven.io.ParentPomParser;
import fitnesse.maven.io.PomFile;

import java.io.File;


public class MavenPomResolver {
    private FileUtil fileUtil;
    private ParentPomParser parentPomParser;

    public MavenPomResolver() {
        this(new FileUtil(), new ParentPomParser());
    }

    protected MavenPomResolver(FileUtil fileUtil, ParentPomParser parentPomParser) {
        this.fileUtil = fileUtil;
        this.parentPomParser = parentPomParser;
    }

    public PomFile resolve(File pomFile) {
        File file = getPomFile(pomFile);
        if (file == null) {
            return null;
        }
        PomFile pom = new PomFile(file);
        File parentFile = file;
        PomFile current = null;
        while ((parentFile = parentPomParser.parse(parentFile)) != null) {
            if (current == null) {
                current = new PomFile(parentFile);
                pom.setParent(current);
            } else {
                PomFile temp = new PomFile(parentFile);
                current.setParent(temp);
                current = temp;
            }
        }
        return pom;
    }

    private File getPomFile(File pomFile) {
        if (fileUtil.exists(pomFile)) {
            if (fileUtil.isDirectory(pomFile)) {
                File pom = new File(pomFile, "pom.xml");
                return fileUtil.exists(pom) ? pom : null;
            }
            return pomFile;
        }
        return null;
    }
}
