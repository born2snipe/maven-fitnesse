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

import fitnesse.util.StringUtil;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;


public class MavenDependencyResolverTest extends TestCase {
    private static final File POM_FILE = new File("pom.xml");
    private static final String CONSOLE_OUTPUT = "[INFO] Scanning for projects...\n" +
            "[INFO] Searching repository for plugin with prefix: 'dependency'.\n" +
            "[INFO] ------------------------------------------------------------------------\n" +
            "[INFO] Building FitNesse POM Widget\n" +
            "[INFO]    task-segment: [dependency:build-classpath]\n" +
            "[INFO] ------------------------------------------------------------------------\n" +
            "[INFO] [dependency:build-classpath]\n" +
            "[INFO] Dependencies classpath:\n" +
            "{0}\n" +
            "[INFO] ------------------------------------------------------------------------\n" +
            "[INFO] BUILD SUCCESSFUL\n" +
            "[INFO] ------------------------------------------------------------------------\n" +
            "[INFO] Total time: 6 seconds\n" +
            "[INFO] Finished at: Mon Apr 20 17:54:34 CDT 2009\n" +
            "[INFO] Final Memory: 20M/52M\n" +
            "[INFO] ------------------------------------------------------------------------\n";

    private MavenDependencyResolver resolver;
    private CommandShell commandShell;
    private DependencyCache dependencyCache;
    private String originalOs;

    protected void setUp() throws Exception {
        super.setUp();
        originalOs = System.getProperty("os.name");
        commandShell = mock(CommandShell.class);
        dependencyCache = mock(DependencyCache.class);
        resolver = new MavenDependencyResolver(commandShell, dependencyCache);
    }

    protected void tearDown() {
        System.setProperty("os.name", originalOs);
    }

    public void test_Cached() {
        List<String> dependencies = Arrays.asList("junit.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(false);
        when(dependencyCache.getDependencies(POM_FILE)).thenReturn(dependencies);

        assertSame(dependencies, resolver.resolve(POM_FILE));
    }

    public void test_multipleDependencies_NotCached_Windows() {
        System.setProperty("os.name", "windows");

        String consoleOutput = createMavenOutput(";", "/classworlds-1.1.jar", "/junit.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
        when(commandShell.execute(POM_FILE.getParentFile(), "mvn.bat", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(consoleOutput);

        List<String> expectedDependencies = Arrays.asList("/classworlds-1.1.jar", "/junit.jar");
        assertEquals(expectedDependencies, resolver.resolve(POM_FILE));

        verify(dependencyCache).cache(POM_FILE, expectedDependencies);
    }

    public void test_multipleDependencies_NotCached_Unix() {
        System.setProperty("os.name", "mac");

        String consoleOutput = createMavenOutput(":", "/classworlds-1.1.jar", "/junit.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
        when(commandShell.execute(POM_FILE.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(consoleOutput);

        List<String> expectedDependencies = Arrays.asList("/classworlds-1.1.jar", "/junit.jar");
        assertEquals(expectedDependencies, resolver.resolve(POM_FILE));

        verify(dependencyCache).cache(POM_FILE, expectedDependencies);
    }

    public void test_singleDependency_NotCached() {
        System.setProperty("os.name", "mac");

        String consoleOutput = createMavenOutput(";", "/classworlds-1.1.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
        when(commandShell.execute(POM_FILE.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(consoleOutput);

        List<String> expectedDependencies = Arrays.asList("/classworlds-1.1.jar");
        assertEquals(expectedDependencies, resolver.resolve(POM_FILE));

        verify(dependencyCache).cache(POM_FILE, expectedDependencies);
    }

    private String createMavenOutput(String delimiter, String... jars) {
        return MessageFormat.format(CONSOLE_OUTPUT, StringUtil.join(Arrays.asList(jars), delimiter));
    }


}
