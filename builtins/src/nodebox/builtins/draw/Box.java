package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PGraphics;

@Description("Draw a 3-dimensional cube or box.")
@Category("Draw")
public class Box extends Node {

    public FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 150);
    public FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 150);
    public FloatPort pZ = new FloatPort(this, "z", Port.Direction.INPUT, 0);
    public FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 100);
    public FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 100);
    public FloatPort pDepth = new FloatPort(this, "depth", Port.Direction.INPUT, 100);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        g.pushMatrix();
        g.translate(pX.get(), pY.get(), pZ.get());
        g.box(pWidth.get(), pHeight.get(), pDepth.get());
        g.popMatrix();
    }
}
