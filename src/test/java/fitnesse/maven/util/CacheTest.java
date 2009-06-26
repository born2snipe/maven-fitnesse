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


public class CacheTest extends TestCase {
    private InMemoryCache.IsOutOfDateHandler handler;
    private InMemoryCache<String, File> cache;
    private static final File VALUE = new File("value");
    private static final String KEY = "key";

    protected void setUp() throws Exception {
        super.setUp();
        handler = mock(InMemoryCache.IsOutOfDateHandler.class);
        cache = new InMemoryCache<String, File>(handler);
    }

    public void test_hasChanged_NothingCached() {
        when(handler.hasChanged(VALUE)).thenReturn(true);

        assertTrue(cache.hasChanged(KEY));
    }

    public void test_hasChanged_Changed() {
        cache.cache(KEY, VALUE);

        when(handler.hasChanged(VALUE)).thenReturn(true);

        assertTrue(cache.hasChanged(KEY));
    }

    public void test_hasChanged_NotChanged() {
        cache.cache(KEY, VALUE);

        when(handler.hasChanged(VALUE)).thenReturn(false);

        assertFalse(cache.hasChanged(KEY));
    }


}
