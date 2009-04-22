package fitnesse.maven.util;

import java.io.File;


public class FileUtil {
    public boolean exists(File file) {
        return file != null && file.exists();
    }

    public long lastModified(File file) {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }
}
