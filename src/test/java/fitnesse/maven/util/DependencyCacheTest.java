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
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DependencyCacheTest {
    private static final File POM_FILE = new File("pom.xml");

    private FileUtil fileUtil;
    private DependencyCache cache;

    @Before
    public void setUp() throws Exception {
        fileUtil = mock(FileUtil.class);
        cache = new DependencyCache(fileUtil, "cache");

        File cacheFile = new File(DependencyCache.CACHE_DIR, "cache");
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
    }

    @Test
    public void test_hasChanged_NoCached() {
        assertTrue(cache.hasChanged(POM_FILE));
        assertEquals(new ArrayList(), cache.getDependencies(POM_FILE));
    }

    @Test
    public void test_hasChanged_Unchanged() {
        when(fileUtil.lastModified(POM_FILE)).thenReturn(1L);

        cache.cache(POM_FILE, Arrays.asList("junit.jar"));

        assertFalse(cache.hasChanged(POM_FILE));
        assertEquals(Arrays.asList("junit.jar"), cache.getDependencies(POM_FILE));
    }

    @Test
    public void test_hasChanged_Changed() {
        when(fileUtil.lastModified(POM_FILE)).thenReturn(1L).thenReturn(2L);

        cache.cache(POM_FILE, Arrays.asList("junit.jar"));

        assertTrue(cache.hasChanged(POM_FILE));
    }


}
