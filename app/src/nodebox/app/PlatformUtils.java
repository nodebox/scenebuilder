package nodebox.app;

import com.sun.jna.Platform;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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

    public static BufferedImage loadImageResource(String imageFilename) {
        try {
            InputStream in = AddressBar.class.getResourceAsStream("/" + imageFilename);
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
