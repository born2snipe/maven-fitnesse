package fitnesse.maven.util;

import fitnesse.util.StringUtil;
import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    protected void setUp() throws Exception {
        super.setUp();
        commandShell = mock(CommandShell.class);
        dependencyCache = mock(DependencyCache.class);
        resolver = new MavenDependencyResolver(commandShell, dependencyCache);
    }

    public void test_Cached() {
        List<String> dependencies = Arrays.asList("junit.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(false);
        when(dependencyCache.getDependencies(POM_FILE)).thenReturn(dependencies);

        assertSame(dependencies, resolver.resolve(POM_FILE));
    }

    public void test_multipleDependencies_NotCached_Windows() {
        String consoleOutput = createMavenOutput(";", "/classworlds-1.1.jar", "/junit.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
        when(commandShell.execute(POM_FILE.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(consoleOutput);

        assertEquals(Arrays.asList("/classworlds-1.1.jar", "/junit.jar"), resolver.resolve(POM_FILE));
    }

    public void test_multipleDependencies_NotCached_Unix() {
        String consoleOutput = createMavenOutput(":", "/classworlds-1.1.jar", "/junit.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
        when(commandShell.execute(POM_FILE.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(consoleOutput);

        assertEquals(Arrays.asList("/classworlds-1.1.jar", "/junit.jar"), resolver.resolve(POM_FILE));
    }

    public void test_singleDependency_NotCached() {
        String consoleOutput = createMavenOutput(";", "/classworlds-1.1.jar");

        when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
        when(commandShell.execute(POM_FILE.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(consoleOutput);

        assertEquals(Arrays.asList("/classworlds-1.1.jar"), resolver.resolve(POM_FILE));
    }

    private String createMavenOutput(String delimiter, String... jars) {
        return MessageFormat.format(CONSOLE_OUTPUT, StringUtil.join(Arrays.asList(jars), delimiter));
    }


}
