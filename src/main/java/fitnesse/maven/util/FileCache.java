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

import com.thoughtworks.xstream.XStream;
import fitnesse.maven.io.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class FileCache<K, V> implements Cache<K, V> {
    private FileUtil fileUtil;
    private File cacheFile;
    private IsOutOfDateHandler handler;
    private Serializer serializer;
    private Map<K, V> cache = new HashMap<K, V>();


    public FileCache(File cacheFile, IsOutOfDateHandler handler, Serializer serializer) {
        this(new FileUtil(), cacheFile, handler, serializer);
    }

    public FileCache(FileUtil fileUtil, File cacheFile, IsOutOfDateHandler handler, Serializer serializer) {
        this.fileUtil = fileUtil;
        this.cacheFile = cacheFile;
        this.handler = handler;
        this.serializer = serializer;
    }

    public void cache(K key, V value) {
        if (!(value instanceof Serializable))
            throw new IllegalArgumentException(value.getClass().getName() + " is not serializable");
        cache.put(key, value);
        fileUtil.write(cacheFile, serializer.serialize(cache));
    }

    public V get(K key) {
        V value = cache.get(key);
        if (value == null) {
            if (fileUtil.exists(cacheFile)) {
                cache = (Map<K, V>) serializer.deserialize(fileUtil.read(cacheFile));
                return cache.get(key);
            }
        }
        return value;
    }

    public boolean hasChanged(K key) {
        V value = get(key);
        if (value == null)
            return true;
        return handler.hasChanged(value);
    }

    public static interface IsOutOfDateHandler<V> {
        boolean hasChanged(V value);
    }

    public static interface Serializer {
        String serialize(Object obj);
        Object deserialize(String content);
    }
}
