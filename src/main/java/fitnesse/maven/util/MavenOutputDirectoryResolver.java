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

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class MavenOutputDirectoryResolver {
    private static Pattern TEST_OUTPUT = Pattern.compile("<testOutputDirectory>(.+)</testOutputDirectory>");
    private static Pattern OUTPUT = Pattern.compile("<outputDirectory>(.+)</outputDirectory>");

    private CommandShell shell;
    private DependencyCache cache;

    public MavenOutputDirectoryResolver() {
        this(new CommandShell(), new DependencyCache());
    }

    protected MavenOutputDirectoryResolver(CommandShell shell, DependencyCache cache) {
        this.shell = shell;
        this.cache = cache;
    }

    public List<String> resolve(File pomFile) {
        if (!cache.hasChanged(pomFile))
            return cache.getDependencies(pomFile);

        String consoleOutput = shell.execute(pomFile.getParentFile(), mvnCommand(), "help:effective-pom");
        return Arrays.asList(getRegexGroup(consoleOutput, OUTPUT, 1), getRegexGroup(consoleOutput, TEST_OUTPUT, 1));
    }

    private String getRegexGroup(String text, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        return matcher.group(group);
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private String mvnCommand() {
        if (isWindows()) {
            return "mvn.bat";
        }
        return "mvn";
    }
}
