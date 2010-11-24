package nodebox.builtins.math;

import nodebox.node.*;

@Description("Calculate the reflection of a point through an origin point.")
@Category("Math")
public class Reflect extends Node {

    public FloatPort pX1 = new FloatPort(this, "x1", Port.Direction.INPUT);
    public FloatPort pY1 = new FloatPort(this, "y1", Port.Direction.INPUT);
    public FloatPort pX2 = new FloatPort(this, "x2", Port.Direction.INPUT);
    public FloatPort pY2 = new FloatPort(this, "y2", Port.Direction.INPUT);
    public FloatPort pDistancePct = new FloatPort(this, "distance%", Port.Direction.INPUT, 100);
    public FloatPort pAngleOffset = new FloatPort(this, "angleOffset", Port.Direction.INPUT, 180);
    public FloatPort pOutputX = new FloatPort(this, "outputX", Port.Direction.OUTPUT);
    public FloatPort pOutputY = new FloatPort(this, "outputY", Port.Direction.OUTPUT);

    public Reflect() {
        pOutputX.setDisplayName("x");
        pOutputY.setDisplayName("y");
    }

    @Override
    public void execute(Context context, float time) {
        float distance = (float) Math.sqrt(Math.pow(pX1.get() - pX2.get(), 2) + Math.pow(pY1.get() - pY2.get(), 2));
        float newDistance = (float) (pDistancePct.get() / 100 * distance);
        float angle = (float) Math.toDegrees(Math.atan2(pY2.get() - pY1.get(), pX2.get() - pX1.get()));
        float newAngle = (float) (pAngleOffset.get() + angle);
        float x = (float) (pX1.get() + Math.cos(Math.toRadians(newAngle)) * newDistance);
        float y = (float) (pY2.get() + Math.sin(Math.toRadians(newAngle)) * newDistance);
        pOutputX.set(x);
        pOutputY.set(y);
    }

}
