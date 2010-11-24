package nodebox.builtins.math;

import nodebox.node.*;

@Description("Calculates the location of a point based on angle and distance.")
@Category("Math")
public class Coordinates extends Node {

    public FloatPort pInputX = new FloatPort(this, "inputX", Port.Direction.INPUT);
    public FloatPort pInputY = new FloatPort(this, "inputY", Port.Direction.INPUT);
    public FloatPort pDistance = new FloatPort(this, "distance", Port.Direction.INPUT);
    public FloatPort pAngle = new FloatPort(this, "angle", Port.Direction.INPUT);
    public FloatPort pOutputX = new FloatPort(this, "outputX", Port.Direction.OUTPUT);
    public FloatPort pOutputY = new FloatPort(this, "outputY", Port.Direction.OUTPUT);

    public Coordinates() {
        pInputX.setDisplayName("x");
        pInputY.setDisplayName("y");
        pOutputX.setDisplayName("x");
        pOutputY.setDisplayName("y");    
    }

    @Override
    public void execute(Context context, float time) {
        float x = (float) (pInputX.get() + Math.cos(Math.toRadians(pAngle.get())) * pDistance.get());
        float y = (float) (pInputY.get() + Math.sin(Math.toRadians(pAngle.get())) * pDistance.get());
        pOutputX.set(x);
        pOutputY.set(y);
    }

}
