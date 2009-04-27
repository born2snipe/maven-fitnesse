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
package fitnesse.maven.io;

import java.io.File;


public class PomFile {
    private File file;
    private PomFile parent;

    public PomFile(File file, PomFile parent) {
        this.file = file;
        this.parent = parent;
    }

    public PomFile(File file) {
        this(file, null);
    }

    public File getFile() {
        return file;
    }

    public File getDirectory() {
        return getFile().getParentFile();
    }

    public PomFile getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public String toString() {
        return file.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PomFile pomFile = (PomFile) o;

        if (file != null ? !file.equals(pomFile.file) : pomFile.file != null) return false;
        if (parent != null ? !parent.equals(pomFile.parent) : pomFile.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = file != null ? file.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
