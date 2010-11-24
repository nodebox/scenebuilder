package nodebox.builtins.math;

import nodebox.node.*;


@Description("Calculate the distance between two points.")
@Category("Math")
public class Distance extends Node {

    public FloatPort pX1 = new FloatPort(this, "x1", Port.Direction.INPUT);
    public FloatPort pY1 = new FloatPort(this, "y1", Port.Direction.INPUT);
    public FloatPort pX2 = new FloatPort(this, "x2", Port.Direction.INPUT);
    public FloatPort pY2 = new FloatPort(this, "y2", Port.Direction.INPUT);
    public FloatPort pDistance = new FloatPort(this, "distance", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        float distance = (float) Math.sqrt(Math.pow(pX1.get() - pX2.get(), 2) + Math.pow(pY1.get() - pY2.get(), 2));
        pDistance.set(distance);
    }

}
