package fitnesse.maven.util;

import java.io.File;
import java.util.*;


public class DependencyCache {
    private FileUtil fileUtil;
    private Map<File, CachedPom> cache = new HashMap<File, CachedPom>();

    public DependencyCache() {
        this(new FileUtil());
    }

    protected DependencyCache(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public void cache(File pomFile, List<String> dependencies) {
        cache.put(pomFile, new CachedPom(dependencies, fileUtil.lastModified(pomFile)));
    }

    public boolean hasChanged(File pomFile) {
        CachedPom cachedPom = cache.get(pomFile);
        if (cachedPom == null)
            return true;
        return fileUtil.lastModified(pomFile) != cachedPom.lastModified;
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

        private CachedPom(List<String> dependencies, long lastModified) {
            this.dependencies = Collections.unmodifiableList(dependencies);
            this.lastModified = lastModified;
        }
    }
}
