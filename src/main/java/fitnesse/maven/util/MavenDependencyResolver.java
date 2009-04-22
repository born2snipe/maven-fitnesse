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

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class MavenDependencyResolver {

    private CommandShell commandShell;
    private DependencyCache dependencyCache;

    public MavenDependencyResolver() {
        this(new CommandShell(), new DependencyCache());
    }


    protected MavenDependencyResolver(CommandShell commandShell, DependencyCache dependencyCache) {
        this.commandShell = commandShell;
        this.dependencyCache = dependencyCache;
    }

    public List<String> resolve(File pomFile) {
        if (!dependencyCache.hasChanged(pomFile)) {
            return dependencyCache.getDependencies(pomFile);
        }
        String pathText = grabClassPathFromConsoleOutput(commandShell.execute(pomFile.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test"));
        List<String> dependencies = Arrays.asList(pathText.split(":|;"));
        dependencyCache.cache(pomFile, dependencies);
        return dependencies;
    }

    private String grabClassPathFromConsoleOutput(String output) {
        List<String> lines = Arrays.asList(output.split("\n"));
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("Dependencies classpath")) {
                return lines.get(i + 1).trim();
            }
        }
        return "";
    }
}
