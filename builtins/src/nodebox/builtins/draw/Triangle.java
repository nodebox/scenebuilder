package nodebox.builtins.draw;

import nodebox.node.*;
import nodebox.util.ProcessingSupport;
import processing.core.PGraphics;

import java.awt.*;

@Description("Draw a triangle.")
@Category("Draw")
public class Triangle extends Node {

    public final FloatPort pX1 = new FloatPort(this, "x1", Port.Direction.INPUT, 0f);
    public final FloatPort pY1 = new FloatPort(this, "y1", Port.Direction.INPUT, 0f);
    public final FloatPort pX2 = new FloatPort(this, "x2", Port.Direction.INPUT, 100f);
    public final FloatPort pY2 = new FloatPort(this, "y2", Port.Direction.INPUT, 0f);
    public final FloatPort pX3 = new FloatPort(this, "x3", Port.Direction.INPUT, 100f);
    public final FloatPort pY3 = new FloatPort(this, "y3", Port.Direction.INPUT, 100f);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        ProcessingSupport.setStyle(g, pFill, pStroke, pStrokeWeight);
        g.triangle(pX1.get(), pY1.get(), pX2.get(), pY2.get(), pX3.get(), pY3.get());
    }
}
