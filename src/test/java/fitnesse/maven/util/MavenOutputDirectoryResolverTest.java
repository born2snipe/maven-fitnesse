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

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;


public class MavenOutputDirectoryResolverTest extends TestCase {
	private static final File POM_FILE = new File("pom.xml");
	private CommandShell shell;
	private DependencyCache cache;
	private MavenOutputDirectoryResolver resolver;
	private String originalOs;


	protected void setUp() throws Exception {
		super.setUp();
		originalOs = System.getProperty("os.name");
		shell = mock(CommandShell.class);
		cache = mock(DependencyCache.class);
		resolver = new MavenOutputDirectoryResolver(cache, shell);
	}


	@Override
	protected void tearDown() throws Exception {
		System.setProperty("os.name", originalOs);
	}


	public void test_resolve_BuildError() {
		String consoleOutput = "[INFO] Scanning for projects...\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[ERROR] FATAL ERROR\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[INFO] Error building POM (may not be this project's POM).\n" +
				"\n" +
				"\n" +
				"Project ID: org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT\n" +
				"\n" +
				"Reason: Cannot find parent: org.studentloan.privateloan:privateloan-parent for project: org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT for project org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT\n" +
				"\n" +
				"\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[INFO] Trace\n" +
				"org.apache.maven.reactor.MavenExecutionException: Cannot find parent: org.studentloan.privateloan:privateloan-parent for project: org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT for project org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT\n" +
				"        at org.apache.maven.DefaultMaven.getProjects(DefaultMaven.java:378)\n" +
				"        at org.apache.maven.DefaultMaven.doExecute(DefaultMaven.java:292)\n" +
				"        at org.apache.maven.DefaultMaven.execute(DefaultMaven.java:129)\n" +
				"        at org.apache.maven.cli.MavenCli.main(MavenCli.java:287)\n" +
				"        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
				"        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)\n" +
				"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" +
				"        at java.lang.reflect.Method.invoke(Method.java:597)\n" +
				"        at org.codehaus.classworlds.Launcher.launchEnhanced(Launcher.java:315)\n" +
				"        at org.codehaus.classworlds.Launcher.launch(Launcher.java:255)\n" +
				"        at org.codehaus.classworlds.Launcher.mainWithExitCode(Launcher.java:430)\n" +
				"        at org.codehaus.classworlds.Launcher.main(Launcher.java:375)\n" +
				"Caused by: org.apache.maven.project.ProjectBuildingException: Cannot find parent: org.studentloan.privateloan:privateloan-parent for project: org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT for project org.studentloan.privateloan:privateLoanIntegration:jar:13.5-SNAPSHOT\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.assembleLineage(DefaultMavenProjectBuilder.java:1370)\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.buildInternal(DefaultMavenProjectBuilder.java:821)\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.buildFromSourceFileInternal(DefaultMavenProjectBuilder.java:506)\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.build(DefaultMavenProjectBuilder.java:198)\n" +
				"        at org.apache.maven.DefaultMaven.getProject(DefaultMaven.java:583)\n" +
				"        at org.apache.maven.DefaultMaven.collectProjects(DefaultMaven.java:461)\n" +
				"        at org.apache.maven.DefaultMaven.getProjects(DefaultMaven.java:365)\n" +
				"        ... 11 more\n" +
				"Caused by: org.apache.maven.project.ProjectBuildingException: POM 'org.studentloan.privateloan:privateloan-parent' not found in repository: Unable to download the artifact from any repository\n" +
				"\n" +
				"  org.studentloan.privateloan:privateloan-parent:pom:13.5-SNAPSHOT\n" +
				"\n" +
				"from the specified remote repositories:\n" +
				"  thirdparty (http://buildserver/archiva/repository/thirdparty)\n" +
				" for project org.studentloan.privateloan:privateloan-parent\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.findModelFromRepository(DefaultMavenProjectBuilder.java:603)\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.assembleLineage(DefaultMavenProjectBuilder.java:1366)\n" +
				"        ... 17 more\n" +
				"Caused by: org.apache.maven.artifact.resolver.ArtifactNotFoundException: Unable to download the artifact from any repository\n" +
				"\n" +
				"  org.studentloan.privateloan:privateloan-parent:pom:13.5-SNAPSHOT\n" +
				"\n" +
				"from the specified remote repositories:\n" +
				"  thirdparty (http://buildserver/archiva/repository/thirdparty)\n" +
				"\n" +
				"        at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:212)\n" +
				"        at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:74)\n" +
				"        at org.apache.maven.project.DefaultMavenProjectBuilder.findModelFromRepository(DefaultMavenProjectBuilder.java:556)\n" +
				"        ... 18 more\n" +
				"Caused by: org.apache.maven.wagon.ResourceDoesNotExistException: Unable to download the artifact from any repository\n" +
				"        at org.apache.maven.artifact.manager.DefaultWagonManager.getArtifact(DefaultWagonManager.java:331)\n" +
				"        at org.apache.maven.artifact.resolver.DefaultArtifactResolver.resolve(DefaultArtifactResolver.java:200)\n" +
				"        ... 20 more\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[INFO] Total time: < 1 second\n" +
				"[INFO] Finished at: Fri Apr 24 07:57:00 CDT 2009\n" +
				"[INFO] Final Memory: 1M/508M\n" +
				"[INFO] ------------------------------------------------------------------------\n";

		when(cache.hasChanged(POM_FILE)).thenReturn(true);
		when(shell.execute(POM_FILE.getParentFile(), "mvn.bat", "help:effective-pom")).thenReturn(consoleOutput);

		try {
			resolver.resolve(POM_FILE);
			fail();
		}
		catch (MavenException e) {
			assertEquals(consoleOutput, e.getMessage());
		}
	}


	public void test_resolve_NotCached_Windows() {
		System.setProperty("os.name", "windows");

		String consoleOutput = consoleOutput("C:\\target\\classes", "C:\\target\\test-classes");

		when(cache.hasChanged(POM_FILE)).thenReturn(true);
		when(shell.execute(POM_FILE.getParentFile(), "mvn.bat", "help:effective-pom")).thenReturn(consoleOutput);

		List<String> outputDirs = Arrays.asList("C:\\target\\classes", "C:\\target\\test-classes");
		assertEquals(outputDirs, resolver.resolve(POM_FILE));

		verify(shell).execute(POM_FILE.getParentFile(), "mvn.bat", "help:effective-pom");
		verify(cache).cache(POM_FILE, outputDirs);
	}


	public void test_resolve_NotCached_Unix() {
		System.setProperty("os.name", "mac");

		String consoleOutput = consoleOutput("/target/classes", "/target/test-classes");

		when(cache.hasChanged(POM_FILE)).thenReturn(true);
		when(shell.execute(POM_FILE.getParentFile(), "mvn", "help:effective-pom")).thenReturn(consoleOutput);

		List<String> outputDirs = Arrays.asList("/target/classes", "/target/test-classes");
		assertEquals(outputDirs, resolver.resolve(POM_FILE));

		verify(shell).execute(POM_FILE.getParentFile(), "mvn", "help:effective-pom");
		verify(cache).cache(POM_FILE, outputDirs);
	}


	public void test_execute_Cached() {
		List<String> directories = Arrays.asList("/target/classes");

		when(cache.hasChanged(POM_FILE)).thenReturn(false);
		when(cache.getDependencies(POM_FILE)).thenReturn(directories);

		assertEquals(directories, resolver.resolve(POM_FILE));
	}


	private String consoleOutput(String outputDir, String testOutputDir) {
		String output = "[INFO] Scanning for projects...\n" +
				"[INFO] Searching repository for plugin with prefix: 'help'.\n" +
				"[INFO] org.apache.maven.plugins: checking for updates from mc-release\n" +
				"[INFO] org.codehaus.mojo: checking for updates from mc-release\n" +
				"[INFO] artifact org.apache.maven.plugins:maven-help-plugin: checking for updates from mc-release\n" +
				"[INFO] artifact org.apache.maven.plugins:maven-help-plugin: checking for updates from central\n" +
				"Downloading: http://repo1.maven.org/maven2/org/apache/maven/plugins/maven-help-plugin/2.1/maven-help-plugin-2.1.pom\n" +
				"9K downloaded\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/org/apache/maven/plugins/maven-help-plugin/2.1/maven-help-plugin-2.1.jar\n" +
				"Downloading: http://repo1.maven.org/maven2/org/apache/maven/plugins/maven-help-plugin/2.1/maven-help-plugin-2.1.jar\n" +
				"58K downloaded\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[INFO] Building maven-fitnesse\n" +
				"[INFO]    task-segment: [help:effective-pom] (aggregator-style)\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/org/apache/maven/plugin-tools/maven-plugin-tools-api/2.4.3/maven-plugin-tools-api-2.4.3.pom\n" +
				"Downloading: http://repo1.maven.org/maven2/org/apache/maven/plugin-tools/maven-plugin-tools-api/2.4.3/maven-plugin-tools-api-2.4.3.pom\n" +
				"4K downloaded\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/org/apache/maven/plugin-tools/maven-plugin-tools/2.4.3/maven-plugin-tools-2.4.3.pom\n" +
				"Downloading: http://repo1.maven.org/maven2/org/apache/maven/plugin-tools/maven-plugin-tools/2.4.3/maven-plugin-tools-2.4.3.pom\n" +
				"9K downloaded\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/com/thoughtworks/xstream/xstream/1.3/xstream-1.3.pom\n" +
				"Downloading: http://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.3/xstream-1.3.pom\n" +
				"11K downloaded\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/com/thoughtworks/xstream/xstream-parent/1.3/xstream-parent-1.3.pom\n" +
				"Downloading: http://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream-parent/1.3/xstream-parent-1.3.pom\n" +
				"13K downloaded\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/org/apache/maven/plugin-tools/maven-plugin-tools-api/2.4.3/maven-plugin-tools-api-2.4.3.jar\n" +
				"Downloading: http://repo1.maven.org/maven2/org/apache/maven/plugin-tools/maven-plugin-tools-api/2.4.3/maven-plugin-tools-api-2.4.3.jar\n" +
				"50K downloaded\n" +
				"Downloading: http://mc-repo.googlecode.com/svn/maven2/releases/com/thoughtworks/xstream/xstream/1.3/xstream-1.3.jar\n" +
				"Downloading: http://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.3/xstream-1.3.jar\n" +
				"401K downloaded\n" +
				"[INFO] [help:effective-pom]\n" +
				"[INFO] \n" +
				"Effective POMs, after inheritance, interpolation, and profiles are applied:\n" +
				"\n" +
				"<!-- ====================================================================== -->\n" +
				"<!--                                                                        -->\n" +
				"<!-- Generated by Maven Help Plugin on 4/23/09 6:55 PM                      -->\n" +
				"<!-- See: http://maven.apache.org/plugins/maven-help-plugin/                -->\n" +
				"<!--                                                                        -->\n" +
				"<!-- ====================================================================== -->\n" +
				"\n" +
				"<!-- ====================================================================== -->\n" +
				"<!--                                                                        -->\n" +
				"<!-- Effective POM for project 'fitnesse:maven-fitnesse:jar:1.2-SNAPSHOT'   -->\n" +
				"<!--                                                                        -->\n" +
				"<!-- ====================================================================== -->\n" +
				"\n" +
				"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
				"  <modelVersion>4.0.0</modelVersion>\n" +
				"  <groupId>fitnesse</groupId>\n" +
				"  <artifactId>maven-fitnesse</artifactId>\n" +
				"  <name>maven-fitnesse</name>\n" +
				"  <version>1.2-SNAPSHOT</version>\n" +
				"  <scm>\n" +
				"    <developerConnection>scm:git:git://github.com/born2snipe/maven-fitnesse.git</developerConnection>\n" +
				"  </scm>\n" +
				"  <build>\n" +
				"    <sourceDirectory>/Users/dan/projects/maven-fitnesse/src/main/java</sourceDirectory>\n" +
				"    <scriptSourceDirectory>src/main/scripts</scriptSourceDirectory>\n" +
				"    <testSourceDirectory>/Users/dan/projects/maven-fitnesse/src/test/java</testSourceDirectory>\n" +
				"    <outputDirectory>{0}</outputDirectory>\n" +
				"    <testOutputDirectory>{1}</testOutputDirectory>\n" +
				"    <resources>\n" +
				"      <resource>\n" +
				"        <directory>/Users/dan/projects/maven-fitnesse/src/main/resources</directory>\n" +
				"      </resource>\n" +
				"    </resources>\n" +
				"    <testResources>\n" +
				"      <testResource>\n" +
				"        <directory>/Users/dan/projects/maven-fitnesse/src/test/resources</directory>\n" +
				"      </testResource>\n" +
				"    </testResources>\n" +
				"    <directory>/Users/dan/projects/maven-fitnesse/target</directory>\n" +
				"    <finalName>maven-fitnesse-1.2-SNAPSHOT</finalName>\n" +
				"    <pluginManagement>\n" +
				"      <plugins>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-antrun-plugin</artifactId>\n" +
				"          <version>1.1</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-assembly-plugin</artifactId>\n" +
				"          <version>2.2-beta-2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-clean-plugin</artifactId>\n" +
				"          <version>2.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-compiler-plugin</artifactId>\n" +
				"          <version>2.0.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-dependency-plugin</artifactId>\n" +
				"          <version>2.0</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-deploy-plugin</artifactId>\n" +
				"          <version>2.3</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-ear-plugin</artifactId>\n" +
				"          <version>2.3.1</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-ejb-plugin</artifactId>\n" +
				"          <version>2.1</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-install-plugin</artifactId>\n" +
				"          <version>2.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-jar-plugin</artifactId>\n" +
				"          <version>2.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-javadoc-plugin</artifactId>\n" +
				"          <version>2.4</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-plugin-plugin</artifactId>\n" +
				"          <version>2.4.1</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-rar-plugin</artifactId>\n" +
				"          <version>2.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-release-plugin</artifactId>\n" +
				"          <version>2.0-beta-7</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-resources-plugin</artifactId>\n" +
				"          <version>2.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-site-plugin</artifactId>\n" +
				"          <version>2.0-beta-6</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-source-plugin</artifactId>\n" +
				"          <version>2.0.4</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-surefire-plugin</artifactId>\n" +
				"          <version>2.4.2</version>\n" +
				"        </plugin>\n" +
				"        <plugin>\n" +
				"          <artifactId>maven-war-plugin</artifactId>\n" +
				"          <version>2.1-alpha-1</version>\n" +
				"        </plugin>\n" +
				"      </plugins>\n" +
				"    </pluginManagement>\n" +
				"    <plugins>\n" +
				"      <plugin>\n" +
				"        <artifactId>maven-compiler-plugin</artifactId>\n" +
				"        <version>2.0.2</version>\n" +
				"        <configuration>\n" +
				"          <source>1.5</source>\n" +
				"          <target>1.5</target>\n" +
				"        </configuration>\n" +
				"      </plugin>\n" +
				"      <plugin>\n" +
				"        <artifactId>maven-release-plugin</artifactId>\n" +
				"        <version>2.0-beta-8</version>\n" +
				"        <configuration>\n" +
				"          <autoVersionSubmodules>true</autoVersionSubmodules>\n" +
				"        </configuration>\n" +
				"      </plugin>\n" +
				"      <plugin>\n" +
				"        <groupId>com.google.code.maven-license-plugin</groupId>\n" +
				"        <artifactId>maven-license-plugin</artifactId>\n" +
				"        <version>1.4.0-rc1</version>\n" +
				"        <executions>\n" +
				"          <execution>\n" +
				"            <phase>process-resources</phase>\n" +
				"            <goals>\n" +
				"              <goal>format</goal>\n" +
				"            </goals>\n" +
				"          </execution>\n" +
				"        </executions>\n" +
				"        <configuration>\n" +
				"          <basedir>/Users/dan/projects/maven-fitnesse</basedir>\n" +
				"          <header>/Users/dan/projects/maven-fitnesse/header.txt</header>\n" +
				"          <quiet>false</quiet>\n" +
				"          <failIfMissing>true</failIfMissing>\n" +
				"          <aggregate>false</aggregate>\n" +
				"          <includes>\n" +
				"            <include>src/**</include>\n" +
				"            <include>**/test/**</include>\n" +
				"          </includes>\n" +
				"          <excludes>\n" +
				"            <exclude>**/test/resources/**</exclude>\n" +
				"          </excludes>\n" +
				"          <useDefaultExcludes>true</useDefaultExcludes>\n" +
				"          <useDefaultMapping>true</useDefaultMapping>\n" +
				"          <encoding>UTF-8</encoding>\n" +
				"        </configuration>\n" +
				"      </plugin>\n" +
				"      <plugin>\n" +
				"        <artifactId>maven-help-plugin</artifactId>\n" +
				"        <version>2.1</version>\n" +
				"      </plugin>\n" +
				"    </plugins>\n" +
				"  </build>\n" +
				"  <repositories>\n" +
				"    <repository>\n" +
				"      <snapshots>\n" +
				"        <enabled>false</enabled>\n" +
				"      </snapshots>\n" +
				"      <id>central</id>\n" +
				"      <name>Maven Repository Switchboard</name>\n" +
				"      <url>http://repo1.maven.org/maven2</url>\n" +
				"    </repository>\n" +
				"  </repositories>\n" +
				"  <pluginRepositories>\n" +
				"    <pluginRepository>\n" +
				"      <releases />\n" +
				"      <snapshots>\n" +
				"        <enabled>false</enabled>\n" +
				"      </snapshots>\n" +
				"      <id>mc-release</id>\n" +
				"      <name>Local Maven repository of releases</name>\n" +
				"      <url>http://mc-repo.googlecode.com/svn/maven2/releases</url>\n" +
				"    </pluginRepository>\n" +
				"    <pluginRepository>\n" +
				"      <releases>\n" +
				"        <updatePolicy>never</updatePolicy>\n" +
				"      </releases>\n" +
				"      <snapshots>\n" +
				"        <enabled>false</enabled>\n" +
				"      </snapshots>\n" +
				"      <id>central</id>\n" +
				"      <name>Maven Plugin Repository</name>\n" +
				"      <url>http://repo1.maven.org/maven2</url>\n" +
				"    </pluginRepository>\n" +
				"  </pluginRepositories>\n" +
				"  <dependencies>\n" +
				"    <dependency>\n" +
				"      <groupId>junit</groupId>\n" +
				"      <artifactId>junit</artifactId>\n" +
				"      <version>3.8.1</version>\n" +
				"      <scope>test</scope>\n" +
				"    </dependency>\n" +
				"    <dependency>\n" +
				"      <groupId>org.mockito</groupId>\n" +
				"      <artifactId>mockito-all</artifactId>\n" +
				"      <version>1.6</version>\n" +
				"      <scope>test</scope>\n" +
				"    </dependency>\n" +
				"    <dependency>\n" +
				"      <groupId>org.fitnesse</groupId>\n" +
				"      <artifactId>fitnesse</artifactId>\n" +
				"      <version>20080812</version>\n" +
				"    </dependency>\n" +
				"  </dependencies>\n" +
				"  <reporting>\n" +
				"    <outputDirectory>target/site</outputDirectory>\n" +
				"  </reporting>\n" +
				"</project>\n" +
				"\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[INFO] BUILD SUCCESSFUL\n" +
				"[INFO] ------------------------------------------------------------------------\n" +
				"[INFO] Total time: 37 seconds\n" +
				"[INFO] Finished at: Thu Apr 23 18:55:03 CDT 2009\n" +
				"[INFO] Final Memory: 8M/22M\n" +
				"[INFO] ------------------------------------------------------------------------\n";
		return MessageFormat.format(output, outputDir, testOutputDir);
	}
}
