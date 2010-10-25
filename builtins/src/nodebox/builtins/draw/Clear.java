package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;


@Description("Clear the canvas.")
public class Clear extends Node {

    public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.LIGHT_GRAY);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        Color c = pColor.get();
        g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
