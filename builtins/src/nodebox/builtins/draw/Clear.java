package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PApplet;

import java.awt.*;


@Description("Clear the canvas.")
public class Clear extends DrawingNode {

    public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.LIGHT_GRAY);

    @Override
    public void draw(PApplet g, Context context, float time) {
        Color c = pColor.get();
        g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
