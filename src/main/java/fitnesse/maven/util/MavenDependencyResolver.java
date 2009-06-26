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

import java.util.Arrays;
import java.util.List;


public class MavenDependencyResolver extends AbstractMavenDependencyResolver {
    private Sys sys;

    public MavenDependencyResolver() {
        this(new MavenCommandShell(), new DependencyCache("dependencies"), new Sys());
    }


    protected MavenDependencyResolver(MavenCommandShell commandShell, DependencyCache dependencyCache, Sys sys) {
        super(dependencyCache, commandShell);
        this.sys = sys;
    }                                                                  


    protected List<String> handleConsoleOutput(String consoleOutput) {
        return Arrays.asList(grabClassPathFromConsoleOutput(consoleOutput).split(getPathSeperator()));
    }


    protected List<String> mvnArgs() {
        return Arrays.asList("dependency:build-classpath", "-DincludeScope=test");
    }


    private String getPathSeperator() {
        if (sys.isWindows()) {
            return ";";
        }
        return ":";
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
