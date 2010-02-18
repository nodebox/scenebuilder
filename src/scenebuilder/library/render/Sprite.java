package scenebuilder.library.render;

import org.lwjgl.opengl.GL11;
import scenebuilder.model.Context;
import scenebuilder.model.GLImage;
import scenebuilder.model.Node;
import scenebuilder.model.Port;
import scenebuilder.util.GLUtils;

import java.awt.*;

public class Sprite extends Node {

    public static final String PORT_ENABLE = "enable";
    public static final String PORT_X = "x";
    public static final String PORT_Y = "y";
    public static final String PORT_Z = "z";
    public static final String PORT_WIDTH = "width";
    public static final String PORT_HEIGHT = "height";
    public static final String PORT_COLOR = "color";
    public static final String PORT_IMAGE = "image";

    public Sprite() {
        super(Function.Renderer);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Sprite");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Draw an image or color in the given position.");
        addInputPort(Port.Type.BOOLEAN, PORT_ENABLE, true);
        addInputPort(Port.Type.NUMBER, PORT_X, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_Y, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_Z, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_WIDTH, 100.0);
        addInputPort(Port.Type.NUMBER, PORT_HEIGHT, 100.0);
        addInputPort(Port.Type.COLOR, PORT_COLOR, Color.WHITE);
        addInputPort(Port.Type.IMAGE, PORT_IMAGE, "");
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!asBoolean(PORT_ENABLE)) return true;
        GL11.glPushMatrix();
        GL11.glTranslated(asNumber(PORT_X), asNumber(PORT_Y), asNumber(PORT_Z));
        double width = asNumber(PORT_WIDTH);
        double height = asNumber(PORT_HEIGHT);
        double left = asNumber(PORT_X) - asNumber(PORT_WIDTH) / 2;
        double top = asNumber(PORT_Y) - asNumber(PORT_HEIGHT) / 2;
        double right = asNumber(PORT_X) + asNumber(PORT_WIDTH) / 2;
        double bottom = asNumber(PORT_Y) + asNumber(PORT_HEIGHT) / 2;

        if (getPort(PORT_IMAGE).isConnected()) {
            GL11.glColor4f(1f, 1f, 1f, 1f);

            GLImage image = asImage(PORT_IMAGE);
            if (image == null) return false;
            image.bind();

            // draw a quad textured to match the sprite
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2d(left, bottom);

                GL11.glTexCoord2f(0, image.getHeight());
                GL11.glVertex2d(left, top);

                GL11.glTexCoord2f(image.getWidth(), image.getHeight());
                GL11.glVertex2d(right, top);

                GL11.glTexCoord2f(image.getWidth(), 0);
                GL11.glVertex2d(right, bottom);
            }
            GL11.glEnd();
            GLImage.unbind();
        } else {
            GLUtils.setColor(asColor(PORT_COLOR));
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2d(left, top);
                GL11.glVertex2d(left, bottom);
                GL11.glVertex2d(right, bottom);
                GL11.glVertex2d(right, top);
                GL11.glEnd();
            }
        }
        GL11.glPopMatrix();
        return true;
    }
}
