package nodebox.builtins.device;

import nodebox.node.*;
import processing.core.PApplet;

/**
 * Even though mouse values are integers, X & Y coordinates are stored as float for compatibility.
 */
@Description("Provide access to mouse coordinates")
public class Mouse extends Node {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.OUTPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.OUTPUT);
    public final FloatPort pPreviousX = new FloatPort(this, "previousX", Port.Direction.OUTPUT);
    public final FloatPort pPreviousY = new FloatPort(this, "previousY", Port.Direction.OUTPUT);
    public final BooleanPort pMousePressed = new BooleanPort(this, "mousePressed", Port.Direction.OUTPUT);
    public final IntPort pMouseButton = new IntPort(this, "mouseButton", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, double time) {
        PApplet g = context.getApplet();
        pX.set((float) g.mouseX);
        pY.set((float) g.mouseY);
        pPreviousX.set((float) g.pmouseX);
        pPreviousY.set((float) g.pmouseY);
        pMousePressed.set(g.mousePressed);
        pMouseButton.set(g.mouseButton);
    }
}
