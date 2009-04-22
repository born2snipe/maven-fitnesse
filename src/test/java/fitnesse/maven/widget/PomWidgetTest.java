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

import fitnesse.html.HtmlUtil;
import fitnesse.maven.util.FileUtil;
import fitnesse.maven.util.MavenDependencyResolver;
import fitnesse.maven.util.MavenException;
import fitnesse.wikitext.widgets.ParentWidget;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class PomWidgetTest extends TestCase {
    private MavenDependencyResolver mavenDependencyResolver;
    private FileUtil fileUtil;
    private ParentWidget parentWidget;
    private ClasspathWidgetFactory classpathWidgetFactory;

    protected void setUp() throws Exception {
        super.setUp();
        mavenDependencyResolver = mock(MavenDependencyResolver.class);
        fileUtil = mock(FileUtil.class);
        parentWidget = mock(ParentWidget.class);
        classpathWidgetFactory = mock(ClasspathWidgetFactory.class);

        PomWidget.FILE_UTIL = fileUtil;
        PomWidget.MAVEN_DEPENDENCY_RESOLVER = mavenDependencyResolver;
        PomWidget.CLASSPATH_WIDGET_FACTORY = classpathWidgetFactory;
    }

    public void test_resolverThrowsException() throws Exception {
        File pomFile = new File("/blah/pom.xml");

        when(fileUtil.exists(pomFile)).thenReturn(true);
        when(mavenDependencyResolver.resolve(pomFile)).thenThrow(new MavenException("error"));

        PomWidget widget = new PomWidget(parentWidget, "!pom /blah/pom.xml");

        assertEquals(HtmlUtil.metaText("Maven POM: " + pomFile) + HtmlUtil.BRtag + "<pre>error</pre>", widget.render());
    }

    public void test_multipleDependencies() throws Exception {
        File pomFile = new File("/blah/pom.xml");
        List<String> dependencies = Arrays.asList("junit.jar", "jmock.jar");

        when(fileUtil.exists(pomFile)).thenReturn(true);
        when(mavenDependencyResolver.resolve(pomFile)).thenReturn(dependencies);

        new PomWidget(parentWidget, "!pom /blah/pom.xml");

        verify(classpathWidgetFactory).build(parentWidget, dependencies);
        verify(classpathWidgetFactory).build(parentWidget, "/blah/classes");
        verify(classpathWidgetFactory).build(parentWidget, "/blah/test-classes");
    }

    public void test_singleDependency() throws Exception {
        File pomFile = new File("/blah/pom.xml");
        List<String> dependencies = Arrays.asList("junit.jar");

        when(fileUtil.exists(pomFile)).thenReturn(true);
        when(mavenDependencyResolver.resolve(pomFile)).thenReturn(dependencies);

        new PomWidget(parentWidget, "!pom /blah/pom.xml");

        verify(classpathWidgetFactory).build(parentWidget, dependencies);
        verify(classpathWidgetFactory).build(parentWidget, "/blah/classes");
        verify(classpathWidgetFactory).build(parentWidget, "/blah/test-classes");
    }


}
