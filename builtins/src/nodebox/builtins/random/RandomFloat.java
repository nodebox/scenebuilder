package nodebox.builtins.random;

import nodebox.node.*;
import nodebox.util.ProcessingSupport;

@Description("Generates a pseudo-random value between minimum and maximum.")
public class RandomFloat extends Node {

    public FloatPort pMinimum = new FloatPort(this, "minimum", Port.Direction.INPUT, 0f);
    public FloatPort pMaximum = new FloatPort(this, "maximum", Port.Direction.INPUT, 100f);
    public IntPort pSeed = new IntPort(this, "seed", Port.Direction.INPUT, 1);
    public FloatPort pValue = new FloatPort(this, "value", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, double time) {
        float v = ProcessingSupport.random(pMinimum.get(), pMaximum.get(), pSeed.get());
        pValue.set(v);
    }
}
