package nodebox.builtins.math;

import nodebox.node.*;

@Description("Calculates the location of a point based on angle and distance.")
@Category("Math")
public class Coordinates extends Node {

    public FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public FloatPort pDistance = new FloatPort(this, "distance", Port.Direction.INPUT);
    public FloatPort pAngle = new FloatPort(this, "angle", Port.Direction.INPUT);
    public FloatPort pOutputValue = new FloatPort(this, "x1", Port.Direction.OUTPUT);
    public FloatPort pOutputValue2 = new FloatPort(this, "y1", Port.Direction.OUTPUT);


    @Override
    public void execute(Context context, float time) {
        float x1 = (float) (pX.get() + Math.cos(Math.toRadians(pAngle.get())) * pDistance.get());
        float y1 = (float) (pY.get() + Math.sin(Math.toRadians(pAngle.get())) * pDistance.get());
        pOutputValue.set(x1);
        pOutputValue2.set(y1);
    }

}
