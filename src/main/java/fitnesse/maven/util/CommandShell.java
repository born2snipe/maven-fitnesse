package fitnesse.maven.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class CommandShell {
    public String execute(File workingDir, String... commands) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Process process = processBuilder.directory(workingDir).command(commands).start();
            byte buffer[] = new byte[2048];
            int len = -1;
            InputStream input = process.getInputStream();
            while ((len = input.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new String(baos.toByteArray());
    }
}
