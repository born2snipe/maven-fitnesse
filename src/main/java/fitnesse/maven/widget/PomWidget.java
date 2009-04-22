package fitnesse.maven.widget;

import fitnesse.maven.util.FileUtil;
import fitnesse.maven.util.MavenDependencyResolver;
import fitnesse.wikitext.widgets.ClasspathWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PomWidget extends ClasspathWidget {

    public static final String REGEXP = "^!pom [^\r\n]*";

    private static final Pattern pattern = Pattern.compile("^!pom (.*)", Pattern.CASE_INSENSITIVE);
    private File pomFile;

    public PomWidget(ParentWidget parentWidget, String inputText) throws Exception {
        this(parentWidget, inputText, new FileUtil(), new MavenDependencyResolver());
    }

    protected PomWidget(ParentWidget parentWidget, String inputText, FileUtil pomFileUtil, MavenDependencyResolver dependencyResolver) throws Exception {
        super(parentWidget, inputText);
        pomFile = parsePomFile(inputText);
        if (!pomFileUtil.exists(pomFile))
            throw new FileNotFoundException("Could not find POM file '" + pomFile + "'");
    }

    private File parsePomFile(String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return new File(matcher.group(1));
        }
        return null;
    }
}
