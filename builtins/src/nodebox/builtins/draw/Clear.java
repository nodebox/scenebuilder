package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PApplet;

import java.awt.*;


@Description("Clear the canvas.")
public class Clear extends DrawingNode {

    public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.BLACK);

    @Override
    public void execute(Context context, double time) {
        if (!pEnabled.get()) return;
        PApplet g = context.getApplet();
        Color c = pColor.get();
        g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
