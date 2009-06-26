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

import fitnesse.maven.io.MavenCommandShell;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MavenOutputDirectoryResolver extends AbstractMavenDependencyResolver {
    private static final Pattern TEST_OUTPUT = Pattern.compile("<testOutputDirectory>(.+)</testOutputDirectory>");
    private static final Pattern OUTPUT = Pattern.compile("<outputDirectory>(.+)</outputDirectory>");

    public MavenOutputDirectoryResolver() {
        this(new DependencyCache("output-directories"), new MavenCommandShell());
    }

    protected MavenOutputDirectoryResolver(DependencyCache cache, MavenCommandShell shell) {
        super(cache, shell);
    }

    protected List<String> mvnArgs() {
        return Arrays.asList("help:effective-pom");
    }

    protected List<String> handleConsoleOutput(String consoleOutput) {
        return Arrays.asList(getRegexGroup(consoleOutput, OUTPUT, 1), getRegexGroup(consoleOutput, TEST_OUTPUT, 1));
    }

    private String getRegexGroup(String text, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        return matcher.group(group);
    }

}
