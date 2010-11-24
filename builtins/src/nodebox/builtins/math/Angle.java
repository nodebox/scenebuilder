package nodebox.builtins.math;

import nodebox.node.*;

@Description("Calculate the angle between two points.")
@Category("Math")
public class Angle extends Node {

    public FloatPort p1X = new FloatPort(this, "x1", Port.Direction.INPUT);
    public FloatPort p1Y = new FloatPort(this, "y1", Port.Direction.INPUT);
    public FloatPort p2X = new FloatPort(this, "x2", Port.Direction.INPUT);
    public FloatPort p2Y = new FloatPort(this, "y2", Port.Direction.INPUT);
    public FloatPort pAngle = new FloatPort(this, "angle", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        float angle = (float) Math.toDegrees(Math.atan2(p2Y.get() - p1Y.get(), p2X.get() - p1X.get()));
        pAngle.set(angle);
    }

}
