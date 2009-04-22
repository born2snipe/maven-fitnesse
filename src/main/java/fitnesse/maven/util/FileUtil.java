package fitnesse.maven.util;

import java.io.File;


public class FileUtil {
    public boolean exists(File file) {
        return file != null && file.exists();
    }
}
