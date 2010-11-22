package nodebox.builtins.draw;

import nodebox.node.*;
import nodebox.util.ProcessingSupport;
import processing.core.PGraphics;

import java.awt.*;

@Description("Draw a point.")
@Drawable
@Category("Draw")
public class Point extends Node {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);
    public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.INPUT, Color.BLACK);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        ProcessingSupport.setStroke(g, pColor);
        g.point(pX.get(), pY.get());
    }
}
