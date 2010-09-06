package nodebox.builtins.draw;

import nodebox.node.ColorPort;
import nodebox.node.Context;
import nodebox.node.DrawingNode;
import nodebox.node.Port;
import processing.core.PApplet;

import java.awt.*;

public class Clear extends DrawingNode {

    public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.BLACK);

    public Clear() {
        setAttribute(DESCRIPTION_ATTRIBUTE, "Fill the display with a constant color.");
    }
    

    @Override
    public void execute(Context context, double time) {
        if (!pEnabled.get()) return;
        PApplet g = context.getApplet();
        Color c = pColor.get();
        g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
