package nodebox.builtins.device;

import nodebox.node.*;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Even though mouse values are integers, X & Y coordinates are stored as float for compatibility.
 */
@Description("Provide access to mouse coordinates.")
@Category("Device")
public class Mouse extends Node {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.OUTPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.OUTPUT);
    public final FloatPort pPreviousX = new FloatPort(this, "previousX", Port.Direction.OUTPUT);
    public final FloatPort pPreviousY = new FloatPort(this, "previousY", Port.Direction.OUTPUT);
    public final BooleanPort pMousePressed = new BooleanPort(this, "mousePressed", Port.Direction.OUTPUT);
    public final IntPort pMouseButton = new IntPort(this, "mouseButton", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        PApplet applet = context.getApplet();
        pX.set((float) applet.mouseX);
        pY.set((float) applet.mouseY);
        pPreviousX.set((float) applet.pmouseX);
        pPreviousY.set((float) applet.pmouseY);
        pMousePressed.set(applet.mousePressed);
        pMouseButton.set(applet.mouseButton);
    }
}
