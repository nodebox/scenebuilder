package nodebox.builtins.render;

import nodebox.node.ColorPort;
import nodebox.node.Context;
import nodebox.node.Port;
import nodebox.node.RenderingNode;
import processing.core.PApplet;

import java.awt.*;

public class Clear extends RenderingNode {

    public final ColorPort pColor;

    public Clear() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Clear");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Fill the display with a constant color.");
        pColor = (ColorPort) addPort(new ColorPort(this, "color", Port.Direction.INPUT, Color.BLACK));
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!pEnabled.get()) return true;
        PApplet g = context.getApplet();
        Color c = pColor.get();
        g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        return true;
    }
}
