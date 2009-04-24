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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;


public class MavenPomResolverTest extends TestCase {
    private static final File DIR = new File("dir");
    private static final File FILE = new File(DIR, "pom.xml");
    private FileUtil fileUtil;
    private MavenPomResolver resolver;

    protected void setUp() throws Exception {
        super.setUp();
        fileUtil = mock(FileUtil.class);
        resolver = new MavenPomResolver(fileUtil);
    }

    public void test_resolve_File_Exists() {
        when(fileUtil.isDirectory(FILE)).thenReturn(false);
        when(fileUtil.exists(FILE)).thenReturn(true);

        assertSame(FILE, resolver.resolve(FILE));
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
    
    public void test_resolve_Dir_Exist() {
        when(fileUtil.isDirectory(DIR)).thenReturn(true);
        when(fileUtil.exists(DIR)).thenReturn(true);
        when(fileUtil.exists(FILE)).thenReturn(true);

        assertEquals(FILE, resolver.resolve(DIR));
    }
}