package nodebox.builtins.device;

import nodebox.node.*;

/**
 * Even though mouse values are integers, X & Y coordinates are stored as float for compatibility.
 */
public class Mouse extends Node {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.OUTPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.OUTPUT);

    public Mouse() {
    }

    @Override
    public void execute(Context context, double time) {
        pX.set((float) context.getApplet().mouseX);
        pY.set((float) context.getApplet().mouseY);
    }
}
