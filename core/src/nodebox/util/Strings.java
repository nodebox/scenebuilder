package nodebox.util;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

    // Discussion about this pattern at:
    // http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile(String.format("%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
    ));

    private static final Pattern COLOR_PATTERN = Pattern.compile("([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3})");

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

    public static int parseInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean parseBoolean(String s, boolean defaultValue) {
        if ("true".equals(s)) {
            return true;
        } else if ("false".equals(s)) {
            return false;
        } else {
            return defaultValue;
        }
    }

    public static Color parseColor(String s, Color defaultValue) {
        Matcher m = COLOR_PATTERN.matcher(s);
        if (m.matches()) {
            int red = Integer.parseInt(m.group(1));
            int green = Integer.parseInt(m.group(2));
            int blue = Integer.parseInt(m.group(3));
            return new Color(red, green, blue);
        } else {
            return defaultValue;
        }
    }
}
