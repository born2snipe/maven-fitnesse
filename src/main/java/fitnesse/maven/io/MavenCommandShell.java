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

import fitnesse.maven.PomFile;
import fitnesse.maven.util.Sys;
import fitnesse.maven.io.CommandShell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MavenCommandShell {
    private CommandShell shell;
    private Sys sys;

    public MavenCommandShell() {
        this(new CommandShell(), new Sys());
    }

    protected MavenCommandShell(CommandShell shell, Sys sys) {
        this.shell = shell;
        this.sys = sys;
    }

    public String execute(PomFile pomFile, String... args) {
        List<String> params = new ArrayList<String>();
        params.add(mvnCommand());
        params.addAll(Arrays.asList(args));
        return shell.execute(pomFile.getDirectory(), params.toArray(new String[0]));
    }

    protected String mvnCommand() {
        if (sys.isWindows()) {
            return "mvn.bat";
        }
        return "mvn";
    }

    public Object execute(PomFile pomFile, OutputHandler handler, String... args) {
        return handler.handle(execute(pomFile, args));
    }

    public interface OutputHandler {
        public Object handle(String consoleOutput);
    }
}
