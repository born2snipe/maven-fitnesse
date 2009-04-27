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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DependencyCache {
    private Cache<File, CachedPom> cache;
    private FileUtil fileUtil;

    public DependencyCache() {
        this(new FileUtil());
    }

    protected DependencyCache(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        cache = new Cache<File, CachedPom>(new PomHandler(fileUtil));
    }

    public void cache(File pomFile, List<String> dependencies) {
        cache.cache(pomFile, new CachedPom(dependencies, fileUtil.lastModified(pomFile), pomFile));
    }

    public boolean hasChanged(File pomFile) {
        return cache.hasChanged(pomFile);
    }

    public List<String> getDependencies(File pomFile) {
        CachedPom cachedPom = cache.get(pomFile);
        if (cachedPom == null)
            return new ArrayList();
        return cachedPom.dependencies;
    }

    private static class CachedPom {
        public final List<String> dependencies;
        public final long lastModified;
        public final File file;

        private CachedPom(List<String> dependencies, long lastModified, File file) {
            this.file = file;
            this.dependencies = Collections.unmodifiableList(dependencies);
            this.lastModified = lastModified;
        }
    }

    private static class PomHandler implements Cache.IsOutOfDateHandler<CachedPom> {
        private FileUtil fileUtil;

        private PomHandler(FileUtil fileUtil) {
            this.fileUtil = fileUtil;
        }

        public boolean hasChanged(CachedPom value) {
            return fileUtil.lastModified(value.file) != value.lastModified;
        }
    }
}
