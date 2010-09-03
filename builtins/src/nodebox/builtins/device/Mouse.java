package nodebox.builtins.device;

import nodebox.node.*;

/**
 * Even though mouse values are integers, X & Y coordinates are stored as float for compatibility.
 */
public class Mouse extends Node {

    public final FloatPort pX;
    public final FloatPort pY;

    public Mouse() {
        pX = (FloatPort) addPort(new FloatPort(this, "x", Port.Direction.OUTPUT, 0f));
        pY = (FloatPort) addPort(new FloatPort(this, "y", Port.Direction.OUTPUT, 0f));
    }

    @Override
    public boolean execute(Context context, double time) {
        pX.set((float) context.getApplet().mouseX);
        pY.set((float) context.getApplet().mouseY);
        return true;
    }
}
