package nodebox.builtins.render;

import nodebox.node.*;
import processing.core.PApplet;

import java.awt.*;

public class Rect extends RenderingNode {

    public final FloatPort pX;
    public final FloatPort pY;
    public final FloatPort pWidth;
    public final FloatPort pHeight;
    public final ColorPort pFill;
    public final ColorPort pStroke;
    public final FloatPort pStrokeWeight;

    public Rect() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Rect");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Draws a simple rectangle at the given position.");
        pX = (FloatPort) addPort(new FloatPort(this, "x", Port.Direction.INPUT, 0f));
        pY = (FloatPort) addPort(new FloatPort(this, "y", Port.Direction.INPUT, 0f));
        pWidth = (FloatPort) addPort(new FloatPort(this, "width", Port.Direction.INPUT, 100f));
        pHeight = (FloatPort) addPort(new FloatPort(this, "height", Port.Direction.INPUT, 100f));
        pFill = (ColorPort) addPort(new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE));
        pStroke = (ColorPort) addPort(new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK));
        pStrokeWeight = (FloatPort) addPort(new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f));
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!pEnabled.get()) return true;
        PApplet g = context.getApplet();
        Color fill = pFill.get();
        g.fill(fill.getRed(), fill.getGreen(), fill.getBlue(), fill.getAlpha());
        Color stroke = pStroke.get();
        g.stroke(stroke.getRed(), stroke.getGreen(), stroke.getBlue(), stroke.getAlpha());
        g.strokeWeight(pStrokeWeight.get());
        g.rect(pX.get(), pY.get(), pWidth.get(), pHeight.get());
        return true;
    }
}
