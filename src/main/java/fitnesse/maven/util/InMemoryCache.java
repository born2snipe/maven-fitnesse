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

import java.util.HashMap;
import java.util.Map;

public class InMemoryCache<K, V> implements Cache<K, V> {
    private IsOutOfDateHandler handler;
    private Map<K, V> cache = new HashMap<K, V>();

    public InMemoryCache(IsOutOfDateHandler handler) {
        this.handler = handler;
    }

    public void cache(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public boolean hasChanged(K key) {
        V value = cache.get(key);
        if (value == null) {
            return true;
        }
        return handler.hasChanged(value);
    }

    public static interface IsOutOfDateHandler<V> {
        boolean hasChanged(V value);
    }
}
