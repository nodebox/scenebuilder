package nodebox.builtins.math;

import nodebox.node.*;

@Description("Calculates the angle between two points")


public class Reflect extends Node {

    public FloatPort p1X = new FloatPort(this, "x1", Port.Direction.INPUT);
    public FloatPort p1Y = new FloatPort(this, "y1", Port.Direction.INPUT);
    public FloatPort p2X = new FloatPort(this, "x2", Port.Direction.INPUT);
    public FloatPort p2Y = new FloatPort(this, "y2", Port.Direction.INPUT);
    public FloatPort pd = new FloatPort(this, "d", Port.Direction.INPUT, (float) 1.0);
    public FloatPort pa = new FloatPort(this, "a", Port.Direction.INPUT, 180);
    public FloatPort pOutputValue = new FloatPort(this, "x", Port.Direction.OUTPUT);
    public FloatPort pOutputValue2 = new FloatPort(this, "y", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        float dtemp = (float) Math.sqrt(Math.pow(p1X.get() - p2X.get(), 2) + Math.pow(p1Y.get() - p2Y.get(), 2));
        float newd = (float) (pd.get() * dtemp);
        float atemp = (float) Math.toDegrees(Math.atan2(p2Y.get() - p1Y.get(), p2X.get() - p1X.get()));
        float newa = (float) (pa.get() + atemp);
        float x1 = (float) (p1X.get() + Math.cos(Math.toRadians(newa)) * newd);
        float y1 = (float) (p2Y.get() + Math.sin(Math.toRadians(newa)) * newd);
        pOutputValue.set(x1);
        pOutputValue2.set(y1);
    }

}
