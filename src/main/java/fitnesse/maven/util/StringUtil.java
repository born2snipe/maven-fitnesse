package fitnesse.maven.util;

import java.util.List;


public class StringUtil {

    public static String join(String delimeter, String... values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i < values.length - 1)
                builder.append(delimeter);
        }
        return builder.toString();
    }

    public static String join(String delimeter, List<String> values) {
        return join(delimeter, values.toArray(new String[0]));
    }
}
