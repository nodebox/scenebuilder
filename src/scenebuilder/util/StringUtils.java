package scenebuilder.util;

public class StringUtils {
    public static String humanizeName(String name) {
           StringBuffer sb = new StringBuffer();
           String[] tokens = name.split("_");
           for (String t : tokens) {
               if (t.length() == 0) continue;
               sb.append(t.substring(0, 1).toUpperCase());
               sb.append(t.substring(1));
               sb.append(" ");
           }
           return sb.toString().trim();
       }
    
}
