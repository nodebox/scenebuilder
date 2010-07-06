package scenebuilder.library.render;

import processing.core.PApplet;
import scenebuilder.model.Context;
import scenebuilder.model.Port;
import scenebuilder.model.RenderingNode;

import java.awt.*;

public class Clear extends RenderingNode {

    public static final String PORT_ENABLE = "enable";
    public static final String PORT_COLOR = "color";

    public Clear() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Clear");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Fill the display with a constant color.");
        addInputPort(Port.Type.BOOLEAN, PORT_ENABLE, true);
        addInputPort(Port.Type.COLOR, PORT_COLOR, Color.GRAY);
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!asBoolean(PORT_ENABLE)) return true;
        PApplet g = context.getApplet();
        Color c = asColor(PORT_COLOR);
        g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        return true;
    }
}
