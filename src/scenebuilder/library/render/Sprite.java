package scenebuilder.library.render;

import processing.core.PApplet;
import scenebuilder.model.Context;
import scenebuilder.model.Port;
import scenebuilder.model.RenderingNode;

import java.awt.*;

public class Sprite extends RenderingNode {

    public static final String PORT_ENABLE = "enable";
    public static final String PORT_X = "x";
    public static final String PORT_Y = "y";
    public static final String PORT_Z = "z";
    public static final String PORT_ROTATION = "rotation";
    public static final String PORT_WIDTH = "width";
    public static final String PORT_HEIGHT = "height";
    public static final String PORT_COLOR = "color";
    public static final String PORT_IMAGE = "image";

    public Sprite() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Sprite");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Draw an image or color in the given position.");
        addInputPort(Port.Type.BOOLEAN, PORT_ENABLE, true);
        addInputPort(Port.Type.NUMBER, PORT_X, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_Y, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_Z, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_ROTATION, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_WIDTH, 100.0);
        addInputPort(Port.Type.NUMBER, PORT_HEIGHT, 100.0);
        addInputPort(Port.Type.COLOR, PORT_COLOR, Color.WHITE);
        addInputPort(Port.Type.IMAGE, PORT_IMAGE, "");
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!asBoolean(PORT_ENABLE)) return true;
        PApplet g = context.getApplet();
        g.pushMatrix();
        g.translate((float)asNumber(PORT_X), (float)asNumber(PORT_Y));
        g.rotate((float) asNumber(PORT_ROTATION));
        double width = asNumber(PORT_WIDTH);
        double height = asNumber(PORT_HEIGHT);
        double left = -width / 2;
        double top = -height / 2;
        double right = width / 2;
        double bottom = height / 2;

        if (getPort(PORT_IMAGE).isConnected()) {
//            GL11.glColor4f(1f, 1f, 1f, 1f);
//
//            PImage image = asImage(PORT_IMAGE);
//            if (image == null) return false;
//            image.bind();
//
//            // draw a quad textured to match the sprite
//            GL11.glBegin(GL11.GL_QUADS);
//            {
//                GL11.glTexCoord2f(0, 0);
//                GL11.glVertex2d(left, bottom);
//
//                GL11.glTexCoord2f(0, image.getHeight());
//                GL11.glVertex2d(left, top);
//
//                GL11.glTexCoord2f(image.getWidth(), image.getHeight());
//                GL11.glVertex2d(right, top);
//
//                GL11.glTexCoord2f(image.getWidth(), 0);
//                GL11.glVertex2d(right, bottom);
//            }
//            GL11.glEnd();
        } else {
            Color c = asColor(PORT_COLOR);
            g.fill(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            g.rect((float)left, (float)top, (float)width, (float)height);
        }
        g.popMatrix();
        return true;
    }
}
