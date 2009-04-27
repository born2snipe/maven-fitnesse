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

import fitnesse.maven.io.MavenCommandShell;
import fitnesse.maven.io.PomFile;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LocalRepoResolver {
    private static final Pattern LOCAL_REPO_PATTERN = Pattern.compile("<localRepository>(.+)</localRepository>");
    private MavenCommandShell shell;

    public LocalRepoResolver() {
        this(new MavenCommandShell());
    }

    protected LocalRepoResolver(MavenCommandShell shell) {
        this.shell = shell;
    }

    public File resolve(PomFile pomFile) {
        String consoleOutput = shell.execute(pomFile, "help:effective-settings");
        Matcher matcher = LOCAL_REPO_PATTERN.matcher(consoleOutput);
        matcher.find();
        return new File(matcher.group(1));
    }

}
