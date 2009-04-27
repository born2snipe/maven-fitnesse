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
package fitnesse.maven.io;

import fitnesse.maven.util.LocalRepoResolver;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.*;


public class ParentPomParser {
    private LocalRepoResolver localRepoResolver;
    private Digester digester;

    public ParentPomParser(LocalRepoResolver localRepoResolver) {
        this.localRepoResolver = localRepoResolver;
        this.digester = new Digester();

        digester.addObjectCreate("project/parent", ParentPomBean.class.getName());
        digester.addBeanPropertySetter("project/parent/groupId", "groupId");
        digester.addBeanPropertySetter("project/parent/artifactId", "artifactId");
        digester.addBeanPropertySetter("project/parent/version", "version");
    }

    public File parse(PomFile pomFile) {
        try {
            return parse(pomFile, new FileInputStream(pomFile.getFile()));
        } catch (FileNotFoundException e) {
            throw new MavenException(e);
        }
    }

    protected File parse(PomFile pomFile, InputStream inputStream) {
        try {
            ParentPomBean parentBean = (ParentPomBean) digester.parse(inputStream);
            if (parentBean == null) {
                return null;
            }
            File localRepoDirectory = localRepoResolver.resolve(pomFile);
            return new File(localRepoDirectory, parentBean.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ParentPomBean {
        private String artifactId;
        private String groupId;
        private String version;

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getFilePath() {
            return groupId + "/" + artifactId + "/" + version + "/pom.xml";
        }
    }
}
