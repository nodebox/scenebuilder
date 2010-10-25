package nodebox.builtins.math;

import nodebox.node.*;


@Description("Calculates the distance between two points")

public class Distance extends Node {

    public FloatPort p1X = new FloatPort(this, "x1", Port.Direction.INPUT);
    public FloatPort p1Y = new FloatPort(this, "y1", Port.Direction.INPUT);
    public FloatPort p2X = new FloatPort(this, "x2", Port.Direction.INPUT);
    public FloatPort p2Y = new FloatPort(this, "y2", Port.Direction.INPUT);
    public FloatPort pOutputValue = new FloatPort(this, "outputValue", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        float value = (float) Math.sqrt(Math.pow(p1X.get() - p2X.get(), 2) + Math.pow(p1Y.get() - p2Y.get(), 2));
        pOutputValue.set(value);
    }

}
