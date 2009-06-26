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
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class FileCacheTest extends TestCase {
    private static final File CACHE_FILE = new File(".maven-fitnesse-cache");
    private FileUtil fileUtil;
    private FileCache<String, Object> cache;
    private FileCache.IsOutOfDateHandler handler;

    protected void setUp() throws Exception {
        super.setUp();
        fileUtil = mock(FileUtil.class);
        handler = mock(FileCache.IsOutOfDateHandler.class);
        cache = new FileCache<String, Object>(fileUtil, CACHE_FILE, handler);
    }

    public void test_hasChanged_HasChanged() {
        cache.cache("key", "value");

        when(handler.hasChanged("value")).thenReturn(true);

        assertTrue(cache.hasChanged("key"));
    }

    public void test_hasChanged_HasNotChanged() {
        cache.cache("key", "value");

        when(handler.hasChanged("value")).thenReturn(false);

        assertFalse(cache.hasChanged("key"));
    }

    public void test_hasChanged_NotInCache() {
        assertTrue(cache.hasChanged("key"));
    }

    public void test_get_CouldNotFindInCacheFileDoesNotExist() {
        when(fileUtil.exists(CACHE_FILE)).thenReturn(false);

        assertNull(cache.get("key"));
    }

    public void test_get_CouldNotFindInCacheIsInFile() {
        Map<String, Object> fileCache = new HashMap<String, Object>();
        fileCache.put("key", "value");

        when(fileUtil.exists(CACHE_FILE)).thenReturn(true);
        when(fileUtil.read(CACHE_FILE)).thenReturn(fileCache);

        assertEquals("value", cache.get("key"));
    }

    public void test_get_AlreadyLoadedInMemory() {
        cache.cache("key", "value");

        assertEquals("value", cache.get("key"));
    }

    public void test_cache() {
        cache.cache("key", "value");

        Map<String, Object> values = new HashMap<String, Object>();
        values.put("key", "value");

        verify(fileUtil).write(CACHE_FILE, values);
    }

    public void test_cache_NotSerializable() {
        try {
            cache.cache("key", new NotSerializable());
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals(NotSerializable.class.getName() + " is not serializable", err.getMessage());
        }
    }


    private static class NotSerializable {

    }
}
