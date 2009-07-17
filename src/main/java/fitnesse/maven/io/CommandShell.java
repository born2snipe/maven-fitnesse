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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;


public class CommandShell {
    public String execute(File workingDir, String... commands) {
        CommandLine commandLine = CommandLine.parse(commands[0]);
        for (int i=1;i<commands.length;i++) {
            commandLine.addArgument(commands[i]);
        }
        DefaultExecutor executor = new DefaultExecutor();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            executor.setStreamHandler(new PumpStreamHandler(baos));
            executor.setWorkingDirectory(workingDir);
            executor.execute(commandLine);
            return new String(baos.toByteArray());
        } catch (ExecuteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
