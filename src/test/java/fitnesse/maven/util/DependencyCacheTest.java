package fitnesse.maven.util;

import junit.framework.TestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;


public class DependencyCacheTest extends TestCase {
    private static final File POM_FILE = new File("pom.xml");

    private FileUtil fileUtil;
    private DependencyCache cache;

    protected void setUp() throws Exception {
        super.setUp();
        fileUtil = mock(FileUtil.class);
        cache = new DependencyCache(fileUtil);
    }

    public void test_hasChanged_NoCached() {
        assertTrue(cache.hasChanged(POM_FILE));
        assertEquals(new ArrayList(), cache.getDependencies(POM_FILE));
    }

    public void test_hasChanged_Unchanged() {
        when(fileUtil.lastModified(POM_FILE)).thenReturn(1L);

        cache.cache(POM_FILE, Arrays.asList("junit.jar"));

        assertFalse(cache.hasChanged(POM_FILE));
        assertEquals(Arrays.asList("junit.jar"), cache.getDependencies(POM_FILE));
    }

    public void test_hasChanged_Changed() {
        when(fileUtil.lastModified(POM_FILE)).thenReturn(1L).thenReturn(2L);

        cache.cache(POM_FILE, Arrays.asList("junit.jar"));

        assertTrue(cache.hasChanged(POM_FILE));
    }


}
