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
import java.util.Arrays;
import java.util.List;


public class MavenCommandShell {
    private CommandShell shell;

    public MavenCommandShell() {
        this(new CommandShell());
    }

    protected MavenCommandShell(CommandShell shell) {
        this.shell = shell;
    }

    public String execute(PomFile pomFile, String... args) {
        List<String> params = new ArrayList<String>();
        params.add(mvnCommand());
        params.addAll(Arrays.asList(args));
        return shell.execute(pomFile.getDirectory(), params.toArray(new String[0]));
    }

    protected String mvnCommand() {
        if (isWindows()) {
            return "mvn.bat";
        }
        return "mvn";
    }

    protected boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
