package nodebox.builtins.math;

import nodebox.node.*;

import static nodebox.util.ProcessingSupport.clamp;

@Description("Given a value and its range, convert the value to the new range")
public class ConvertRange extends Node {

    public FloatPort pValue = new FloatPort(this, "value", Port.Direction.INPUT);
    public FloatPort pSourceMinimum = new FloatPort(this, "sourceMinimum", Port.Direction.INPUT, 0);
    public FloatPort pSourceMaximum = new FloatPort(this, "sourceMaximum", Port.Direction.INPUT, 100);
    public FloatPort pTargetMinimum = new FloatPort(this, "targetMinimum", Port.Direction.INPUT, 0);
    public FloatPort pTargetMaximum = new FloatPort(this, "targetMaximum", Port.Direction.INPUT, 255);
    public FloatPort pOutputValue = new FloatPort(this, "outputValue", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, double time) {
        float srcMin = pSourceMinimum.get();
        float srcMax = pSourceMaximum.get();
        float targetMin = pTargetMinimum.get();
        float targetMax = pTargetMaximum.get();
        float value = clamp(pValue.get(), srcMin, srcMax);
        // Convert value to 0.0-1.0 range.
        try {
            value = (value - srcMin) / (srcMax - srcMin);
        } catch (ArithmeticException e) {
            value = srcMin;
        }
        // Convert value to target range.
        value = targetMin + value * (targetMax - targetMin);
        pOutputValue.set(value);
    }
}
