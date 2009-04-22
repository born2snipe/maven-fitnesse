package fitnesse.maven.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class MavenDependencyResolver {

    private CommandShell commandShell;
    private DependencyCache dependencyCache;

    public MavenDependencyResolver() {
        this(new CommandShell(), new DependencyCache());
    }


    protected MavenDependencyResolver(CommandShell commandShell, DependencyCache dependencyCache) {
        this.commandShell = commandShell;
        this.dependencyCache = dependencyCache;
    }

    public List<String> resolve(File pomFile) {
        if (!dependencyCache.hasChanged(pomFile)) {
            return dependencyCache.getDependencies(pomFile);
        }
        String pathText = grabClassPathFromConsoleOutput(commandShell.execute(pomFile.getParentFile(), "mvn", "dependency:build-classpath", "-DincludeScope=test"));
        return Arrays.asList(pathText.split(":|;"));
    }

    private String grabClassPathFromConsoleOutput(String output) {
        List<String> lines = Arrays.asList(output.split("\n"));
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("Dependencies classpath")) {
                return lines.get(i + 1).trim();
            }
        }
        return "";
    }
}
