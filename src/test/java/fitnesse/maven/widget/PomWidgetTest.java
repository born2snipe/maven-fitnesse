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
package fitnesse.maven.widget;

import fitnesse.maven.util.*;
import fitnesse.wikitext.widgets.ParentWidget;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class PomWidgetTest extends TestCase {
    private ClasspathWidgetFactory classpathWidgetFactory;
    private MavenDependencyResolver mavenDependencyResolver;
    private ParentWidget parentWidget;
    private MavenOutputDirectoryResolver mavenOutputDirectoryResolver;
    private IdGenerator idGenerator;
    private MavenPomResolver mavenPomResolver;


    protected void setUp() throws Exception {
        super.setUp();

        mavenDependencyResolver = mock(MavenDependencyResolver.class);
        parentWidget = mock(ParentWidget.class);
        classpathWidgetFactory = mock(ClasspathWidgetFactory.class);
        mavenOutputDirectoryResolver = mock(MavenOutputDirectoryResolver.class);
        idGenerator = mock(IdGenerator.class);
        mavenPomResolver = mock(MavenPomResolver.class);

        PomWidget.MAVEN_DEPENDENCY_RESOLVER = mavenDependencyResolver;
        PomWidget.CLASSPATH_WIDGET_FACTORY = classpathWidgetFactory;
        PomWidget.MAVEN_OUTPUT_DIR_RESOLVER = mavenOutputDirectoryResolver;
        PomWidget.ID_GENERATOR = idGenerator;
        PomWidget.MAVEN_POM_RESOLVER = mavenPomResolver;
    }


    public void test_resolverThrowsException() throws Exception {
        File pomFile = new File("/blah/pom.xml");

        when(mavenPomResolver.resolve(pomFile)).thenReturn(pomFile);
        when(mavenDependencyResolver.resolve(pomFile)).thenThrow(new MavenException("error"));
        when(idGenerator.generate()).thenReturn("1");

        PomWidget widget = new PomWidget(parentWidget, "!pom /blah/pom.xml");

        String expectedHtml = "<div class=\"collapse_rim\">" +
                "<a href=\"javascript:toggleCollapsable('maven-pom-1');\">" +
                "<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"imgmaven-pom-1\"/>" +
                "</a>" +
                "<b><span class=\"meta\">&nbsp;Maven POM: /blah/pom.xml</span></b>" +
                "<div class=\"collapsable\" id=\"maven-pom-1\"><pre>error</pre></div>" +
                "</div>";

        assertEquals(expectedHtml, removeEndingWhitespace(widget.render()));
    }


    public void test_multipleDependencies() throws Exception {
        File pomFile = new File("/blah/pom.xml");
        List<String> dependencies = Arrays.asList("junit.jar", "jmock.jar");

        when(mavenPomResolver.resolve(pomFile)).thenReturn(pomFile);
        when(mavenOutputDirectoryResolver.resolve(pomFile)).thenReturn(Arrays.asList("/target/classes"));
        when(mavenDependencyResolver.resolve(pomFile)).thenReturn(dependencies);
        when(idGenerator.generate()).thenReturn("1");

        PomWidget widget = new PomWidget(parentWidget, "!pom /blah/pom.xml");

        verify(classpathWidgetFactory).build(widget, Arrays.asList("/target/classes", "junit.jar", "jmock.jar"));
        verifyNoMoreInteractions(classpathWidgetFactory);

        String expectedHtml = "<div class=\"collapse_rim\">" +
                "<a href=\"javascript:toggleCollapsable('maven-pom-1');\">" +
                "<img src=\"/files/images/collapsableClosed.gif\" class=\"left\" id=\"imgmaven-pom-1\"/></a>" +
                "<b><span class=\"meta\">&nbsp;Maven POM: /blah/pom.xml</span></b>" +
                "<div class=\"hidden\" id=\"maven-pom-1\"></div>" +
                "</div>";

        assertEquals(expectedHtml, removeEndingWhitespace(widget.render()));
    }


    public void test_singleDependency() throws Exception {
        File pomFile = new File("/blah/pom.xml");
        List<String> dependencies = Arrays.asList("junit.jar");

        when(mavenPomResolver.resolve(pomFile)).thenReturn(pomFile);
        when(mavenDependencyResolver.resolve(pomFile)).thenReturn(dependencies);
        when(mavenOutputDirectoryResolver.resolve(pomFile)).thenReturn(Arrays.asList("/target/classes"));

        PomWidget widget = new PomWidget(parentWidget, "!pom /blah/pom.xml");

        verify(classpathWidgetFactory).build(widget, Arrays.asList("/target/classes", "junit.jar"));
        verifyNoMoreInteractions(classpathWidgetFactory);
    }


    private String removeEndingWhitespace(String value) {
        return value.replaceAll("\\s{2,10}", "").replaceAll("\n", "");
    }


}
