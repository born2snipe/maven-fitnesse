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

import java.io.*;


public class FileUtil {
    public boolean exists(File file) {
        return file != null && file.exists();
    }

    public long lastModified(File file) {
        return file.lastModified();
    }

    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    public void write(File file, String content) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes("UTF-8"));
            out.flush();
        } catch (IOException err) {
            throw new RuntimeException(err);
        } finally {
            close(out);
        }

    }

    public String read(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            while ((len = input.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return new String(baos.toByteArray(), "UTF-8");
        } finally {
            close(input);
        }
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete(File file) {
        if (file.exists()) file.delete();
    }
}
