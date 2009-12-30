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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SysTest {
    private String original;
    private Sys sys;

    @Before
    public void setUp() throws Exception {
        sys = new Sys();
        original = System.getProperty("os.name");
    }

    @After
    public void tearDown() throws Exception {
        System.setProperty("os.name", original);
    }

    @Test
    public void test_isWindows_Mixedcase() {
        System.setProperty("os.name", "Win");
        assertTrue(sys.isWindows());
    }

    @Test
    public void test_isWindows_Lowercase() {
        System.setProperty("os.name", "win");
        assertTrue(sys.isWindows());
    }

    @Test
    public void test_isWindows_Uppercase() {
        System.setProperty("os.name", "WIN");
        assertTrue(sys.isWindows());
    }

    @Test
    public void test_isWindows_NotWindows() {
        System.setProperty("os.name", "does not matter");
        assertFalse(sys.isWindows());
    }
}
