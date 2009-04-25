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

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractMavenDependencyResolver {
    protected CommandShell commandShell;
    protected DependencyCache dependencyCache;

    protected AbstractMavenDependencyResolver(DependencyCache dependencyCache, CommandShell commandShell) {
        this.dependencyCache = dependencyCache;
        this.commandShell = commandShell;
    }

    public List<String> resolve(PomFile pomFile) {
        if (!dependencyCache.hasChanged(pomFile.getFile())) {
            return dependencyCache.getDependencies(pomFile.getFile());
        }
        List<String> args = new ArrayList<String>();
        args.add(mvnCommand());
        args.addAll(mvnArgs());
        String consoleOutput = commandShell.execute(pomFile.getDirectory(), args.toArray(new String[0]));
        if (buildFailure(consoleOutput)) {
            throw new MavenException(consoleOutput);
        }
        List<String> dependencies = handleConsoleOutput(consoleOutput);
        dependencyCache.cache(pomFile.getFile(), dependencies);
        return dependencies;
    }

    protected abstract List<String> mvnArgs();

    protected abstract List<String> handleConsoleOutput(String consoleOutput);

    protected String mvnCommand() {
        if (isWindows()) {
            return "mvn.bat";
        }
        return "mvn";
    }

    protected boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }


    private boolean buildFailure(String consoleOutput) {
        return consoleOutput.contains("MavenExecutionException") || consoleOutput.contains("BUILD ERROR");
    }
}
