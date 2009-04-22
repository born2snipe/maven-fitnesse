package fitnesse.maven.util;

import junit.framework.TestCase;

import java.util.Arrays;


public class StringUtilTest extends TestCase {
    public void test_join() {
        assertEquals("x", StringUtil.join(",", "x"));
        assertEquals("x", StringUtil.join(",", Arrays.asList("x")));
        assertEquals("x,y,z", StringUtil.join(",", "x", "y", "z"));
        assertEquals("x,y,z", StringUtil.join(",", Arrays.asList("x", "y", "z")));
    }
}
