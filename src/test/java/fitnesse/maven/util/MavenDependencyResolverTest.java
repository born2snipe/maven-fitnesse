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
	private CommandShell commandShell;
	private DependencyCache dependencyCache;

	private MavenDependencyResolver resolver;
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

	public void test_MavenError() {
	    String output = "[INFO] Scanning for projects...\n" +
			    "[INFO] ------------------------------------------------------------------------\n" +
			    "[ERROR] FATAL ERROR\n" +
			    "[INFO] ------------------------------------------------------------------------\n" +
			    "[INFO] Error building POM (may not be this project's POM).\n" +
			    "\n" +
			    "\n" +
			    "Project ID: org.studentloan.privateloan:isl-privateloan:jar:13.4-SNAPSHOT\n" +
			    "\n" +
			    "Reason: Cannot find parent: org.studentloan.privateloan:privateloan-parent for p" +
			    "roject: org.studentloan.privateloan:isl-privateloan:jar:13.4-SNAPSHOT for projec" +
			    "t org.studentloan.privateloan:isl-privateloan:jar:13.4-SNAPSHOT\n" +
			    "\n" +
			    "\n" +
			    "[INFO] ------------------------------------------------------------------------\n" +
			    "[INFO] Trace\n" +
			    "org.apache.maven.reactor.MavenExecutionException: Cannot find parent: org.studen\n" +
			    "tloan.privateloan:privateloan-parent for project: org.studentloan.privateloan:is\n" +
			    "l-privateloan:jar:13.4-SNAPSHOT for project org.studentloan.privateloan:isl-priv\n" +
			    "ateloan:jar:13.4-SNAPSHOT\n" +
			    "        at org.apache.maven.DefaultMaven.getProjects(DefaultMaven.java:378)\n" +
			    "        at org.apache.maven.DefaultMaven.doExecute(DefaultMaven.java:292)\n" +
			    "        at org.apache.maven.DefaultMaven.execute(DefaultMaven.java:129)\n" +
			    "        at org.apache.maven.cli.MavenCli.main(MavenCli.java:287)\n" +
			    "        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
			    "        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.\n" +
			    "java:39)\n" +
			    "        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAcces\n" +
			    "sorImpl.java:25)\n" +
			    "        at java.lang.reflect.Method.invoke(Method.java:597)\n" +
			    "        at org.codehaus.classworlds.Launcher.launchEnhanced(Launcher.java:315)\n" +
			    "        at org.codehaus.classworlds.Launcher.launch(Launcher.java:255)\n" +
			    "        at org.codehaus.classworlds.Launcher.mainWithExitCode(Launcher.java:430)\n" +
			    "\n" +
			    "        at org.codehaus.classworlds.Launcher.main(Launcher.java:375)\n" +
			    "Caused by: org.apache.maven.project.ProjectBuildingException: Cannot find parent\n" +
			    ": org.studentloan.privateloan:privateloan-parent for project: org.studentloan.pr\n" +
			    "ivateloan:isl-privateloan:jar:13.4-SNAPSHOT for project org.studentloan.privatel\n" +
			    "oan:isl-privateloan:jar:13.4-SNAPSHOT\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.assembleLineage(D\n" +
			    "efaultMavenProjectBuilder.java:1370)\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.buildInternal(Def\n" +
			    "aultMavenProjectBuilder.java:821)\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.buildFromSourceFi\n" +
			    "leInternal(DefaultMavenProjectBuilder.java:506)\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.build(DefaultMave\n" +
			    "nProjectBuilder.java:198)\n" +
			    "        at org.apache.maven.DefaultMaven.getProject(DefaultMaven.java:583)\n" +
			    "        at org.apache.maven.DefaultMaven.collectProjects(DefaultMaven.java:461)\n" +
			    "        at org.apache.maven.DefaultMaven.getProjects(DefaultMaven.java:365)\n" +
			    "        ... 11 more\n" +
			    "Caused by: org.apache.maven.project.ProjectBuildingException: POM 'org.studentlo\n" +
			    "an.privateloan:privateloan-parent' not found in repository: Unable to download t\n" +
			    "he artifact from any repository\n" +
			    "\n" +
			    "  org.studentloan.privateloan:privateloan-parent:pom:13.4-SNAPSHOT\n" +
			    "\n" +
			    "from the specified remote repositories:\n" +
			    "  thirdparty (http://buildserver/archiva/repository/thirdparty)\n" +
			    " for project org.studentloan.privateloan:privateloan-parent\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.findModelFromRepo\n" +
			    "sitory(DefaultMavenProjectBuilder.java:603)\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.assembleLineage(D\n" +
			    "efaultMavenProjectBuilder.java:1366)\n" +
			    "        ... 17 more\n" +
			    "Caused by: org.apache.maven.artifact.resolver.ArtifactNotFoundException: Unable\n" +
			    "to download the artifact from any repository\n" +
			    "\n" +
			    "  org.studentloan.privateloan:privateloan-parent:pom:13.4-SNAPSHOT\n" +
			    "\n" +
			    "from the specified remote repositories:\n" +
			    "  thirdparty (http://buildserver/archiva/repository/thirdparty)\n" +
			    "\n" +
			    "        at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(De\n" +
			    "faultArtifactResolver.java:212)\n" +
			    "        at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(De\n" +
			    "faultArtifactResolver.java:74)\n" +
			    "        at org.apache.maven.project.DefaultMavenProjectBuilder.findModelFromRepo\n" +
			    "sitory(DefaultMavenProjectBuilder.java:556)\n" +
			    "        ... 18 more\n" +
			    "Caused by: org.apache.maven.wagon.ResourceDoesNotExistException: Unable to downl\n" +
			    "oad the artifact from any repository\n" +
			    "        at org.apache.maven.artifact.manager.DefaultWagonManager.getArtifact(Def\n" +
			    "aultWagonManager.java:331)\n" +
			    "        at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(De\n" +
			    "faultArtifactResolver.java:200)\n" +
			    "        ... 20 more\n" +
			    "[INFO] ------------------------------------------------------------------------\n" +
			    "[INFO] Total time: < 1 second\n" +
			    "[INFO] Finished at: Wed Apr 22 15:43:57 CDT 2009\n" +
			    "[INFO] Final Memory: 1M/508M\n" +
			    "[INFO] ------------------------------------------------------------------------";

		System.setProperty("os.name", "windows");

		when(dependencyCache.hasChanged(POM_FILE)).thenReturn(true);
		when(commandShell.execute(POM_FILE.getParentFile(), "mvn.bat", "dependency:build-classpath", "-DincludeScope=test")).thenReturn(output);

		try {
			resolver.resolve(POM_FILE);
			fail();
		} catch (MavenException err) {
			assertEquals("Reason: Cannot find parent: org.studentloan.privateloan:privateloan-parent for project: " +
					"org.studentloan.privateloan:isl-privateloan:jar:13.4-SNAPSHOT for project " +
					"org.studentloan.privateloan:isl-privateloan:jar:13.4-SNAPSHOT", err.getMessage());
		}
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
