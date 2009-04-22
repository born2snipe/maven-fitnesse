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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class CommandShell {
    public String execute(File workingDir, String... commands) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Process process = processBuilder.directory(workingDir).command(commands).start();
            byte buffer[] = new byte[2048];
            int len = -1;
            InputStream input = process.getInputStream();
            while ((len = input.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new String(baos.toByteArray());
    }
}
