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

import fitnesse.maven.util.Sys;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MavenCommandShellTest {
    private static final PomFile POM_FILE = new PomFile(new File("blah/pom.xml"));
    private CommandShell shell;
    private MavenCommandShell mvnShell;
    private Sys sys;

    @Before
    public void setUp() throws Exception {
        sys = mock(Sys.class);
        shell = mock(CommandShell.class);
        mvnShell = new MavenCommandShell(shell, sys);
    }


    @Test
    public void test_execute_WithOutputHandler() {
        when(sys.isWindows()).thenReturn(false);
        when(shell.execute(POM_FILE.getDirectory(), "mvn", "clean", "install")).thenReturn("1,2,3");

        MavenCommandShell.OutputHandler handler = new MavenCommandShell.OutputHandler() {
            public Object handle(String consoleOutput) {
                return Arrays.asList(consoleOutput.split(","));
            }
        };

        assertEquals(Arrays.asList("1", "2", "3"), mvnShell.execute(POM_FILE, handler, "clean", "install"));
    }

    @Test
    public void test_execute_Unix() {
        when(sys.isWindows()).thenReturn(false);
        when(shell.execute(POM_FILE.getDirectory(), "mvn", "clean", "install")).thenReturn("output");

        assertEquals("output", mvnShell.execute(POM_FILE, "clean", "install"));
    }

    @Test
    public void test_execute_Windows_PomFile() {
        when(sys.isWindows()).thenReturn(true);
        when(shell.execute(POM_FILE.getDirectory(), "mvn.bat", "clean", "install")).thenReturn("windows output");

        assertEquals("windows output", mvnShell.execute(POM_FILE, "clean", "install"));
    }

    @Test
    public void test_execute_Windows_File() {
        when(sys.isWindows()).thenReturn(true);
        when(shell.execute(POM_FILE.getDirectory(), "mvn.bat", "clean", "install")).thenReturn("windows output");

        assertEquals("windows output", mvnShell.execute(POM_FILE.getFile(), "clean", "install"));
    }


}
