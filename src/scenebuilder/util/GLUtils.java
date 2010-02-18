package scenebuilder.util;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GLUtils {

    public static void setColor(Color c) {
        float[] cs = c.getComponents(null);
        //GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f,c.getBlue()/255f,c.getAlpha()/255f);
        GL11.glColor4f(cs[0], cs[1], cs[2], cs[3]);
    }

    public static void setClearColor(Color c) {
        float[] cs = c.getComponents(null);
        GL11.glClearColor(cs[0], cs[1], cs[2], cs[3]);
    }
}
