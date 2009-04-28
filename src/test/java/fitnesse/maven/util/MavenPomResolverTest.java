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

import fitnesse.maven.io.FileUtil;
import fitnesse.maven.io.ParentPomParser;
import fitnesse.maven.io.PomFile;
import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;


public class MavenPomResolverTest extends TestCase {
    private static final File DIR = new File("dir");
    private static final File FILE = new File(DIR, "pom.xml");
    private static final PomFile POM = new PomFile(FILE);
    private FileUtil fileUtil;
    private MavenPomResolver resolver;
    private ParentPomParser parentPomParser;

    protected void setUp() throws Exception {
        super.setUp();
        fileUtil = mock(FileUtil.class);
        parentPomParser = mock(ParentPomParser.class);
        resolver = new MavenPomResolver(fileUtil, parentPomParser);
    }

    public void test_resolve_File_ParentHasParent() {
        File parent = new File("parent/pom.xml");
        File parentOfParent = new File("parentOfParent/pom.xml");

        when(parentPomParser.parse(FILE)).thenReturn(parent);
        when(parentPomParser.parse(parent)).thenReturn(parentOfParent);
        when(fileUtil.isDirectory(FILE)).thenReturn(false);
        when(fileUtil.exists(FILE)).thenReturn(true);

        PomFile pomFile = resolver.resolve(FILE);

        PomFile parentOfParentPom = new PomFile(parentOfParent);
        PomFile parentPom = new PomFile(parent, parentOfParentPom);
        assertEquals(FILE, pomFile.getFile());
        assertEquals(parentPom, pomFile.getParent());
        assertEquals(parentOfParentPom, pomFile.getParent().getParent());
        assertNull(pomFile.getParent().getParent().getParent());
    }

    public void test_resolve_File_HasParent() {
        File parent = new File("parent/pom.xml");

        when(parentPomParser.parse(FILE)).thenReturn(parent);
        when(fileUtil.isDirectory(FILE)).thenReturn(false);
        when(fileUtil.exists(FILE)).thenReturn(true);

        PomFile pomFile = resolver.resolve(FILE);
        assertEquals(FILE, pomFile.getFile());
        assertEquals(new PomFile(parent), pomFile.getParent());
    }

    public void test_resolve_File_Exists_NoParent() {
        when(fileUtil.isDirectory(FILE)).thenReturn(false);
        when(fileUtil.exists(FILE)).thenReturn(true);

        assertEquals(POM, resolver.resolve(FILE));
    }

    public void test_resolve_File_DoesNotExist() {
        when(fileUtil.isDirectory(FILE)).thenReturn(false);
        when(fileUtil.exists(FILE)).thenReturn(false);

        assertNull(resolver.resolve(FILE));
    }

    public void test_resolve_Dir_DoesNotExist() {
        when(fileUtil.isDirectory(DIR)).thenReturn(true);
        when(fileUtil.exists(DIR)).thenReturn(false);

        assertNull(resolver.resolve(FILE));
    }

    public void test_resolve_Dir_Exist_NoParent() {
        when(fileUtil.isDirectory(DIR)).thenReturn(true);
        when(fileUtil.exists(DIR)).thenReturn(true);
        when(fileUtil.exists(FILE)).thenReturn(true);

        assertEquals(POM, resolver.resolve(DIR));
    }
}
