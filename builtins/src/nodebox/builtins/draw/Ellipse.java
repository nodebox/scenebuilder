package nodebox.builtins.draw;

import nodebox.node.*;
import nodebox.util.ProcessingSupport;
import processing.core.PGraphics;

import java.awt.*;

@Description("Draw an ellipse.")
@Category("Draw")
@Drawable
public class Ellipse extends Node {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);
    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 100f);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 100f);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        ProcessingSupport.setStyle(g, pFill, pStroke, pStrokeWeight);
        g.ellipse(pX.get(), pY.get(), pWidth.get(), pHeight.get());
    }

}
