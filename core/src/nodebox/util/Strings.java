package nodebox.util;

import java.util.regex.Pattern;

public class Strings {

    // Discussion about this pattern at:
    // http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile(String.format("%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
    ));

    public static String humanizeName(String s) {
        return CAMEL_CASE_PATTERN.matcher(s).replaceAll(" ");
    }

    public static String classToName(Class c) {
        String simpleName = c.getSimpleName();
        if (simpleName.length() > 1) {
            return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        } else {
            return simpleName.toLowerCase();
        }
    }
}
