package nodebox.app;

import com.sun.jna.Platform;

import javax.swing.*;
import java.awt.*;

public class PlatformUtils {

    public static int platformSpecificModifier;

    static {
          try {
            platformSpecificModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        } catch (HeadlessException e) {
            platformSpecificModifier = Event.CTRL_MASK;
        }
    }

    public static boolean isMac() {
        return Platform.isMac();
    }

    public static boolean isWindows() {
        return Platform.isWindows();
    }

      public static KeyStroke getKeyStroke(int key) {
        return KeyStroke.getKeyStroke(key, platformSpecificModifier);
    }

    public static KeyStroke getKeyStroke(int key, int modifier) {
        return KeyStroke.getKeyStroke(key, platformSpecificModifier | modifier);
    }
}
