package nodebox.builtins.device;

import nodebox.node.*;
import processing.core.PApplet;
import processing.core.PGraphics;

@Description("Provide access to keyboard data.")
@Category("Device")
public class Keyboard extends Node {

    public final StringPort pKey = new StringPort(this, "key", Port.Direction.OUTPUT, "");
    public final IntPort pKeyCode = new IntPort(this, "keyCode", Port.Direction.OUTPUT, 0);
    public final BooleanPort pKeyPressed = new BooleanPort(this, "keyPressed", Port.Direction.OUTPUT, false);

    @Override
    public void execute(Context context, float time) {
        PApplet applet = context.getApplet();
        pKey.set(String.valueOf(applet.key));
        pKeyCode.set(applet.keyCode);
        pKeyPressed.set(applet.keyPressed);
    }
}
