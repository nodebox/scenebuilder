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
    public StringPort pOverflow = new StringPort(this, "overflow", Port.Direction.INPUT, "clamp");
    public FloatPort pOutputValue = new FloatPort(this, "outputValue", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        float srcMin = pSourceMinimum.get();
        float srcMax = pSourceMaximum.get();
        float targetMin = pTargetMinimum.get();
        float targetMax = pTargetMaximum.get();
        String overflow = pOverflow.get();
        float value = pValue.get();
        if (overflow.equals("wrap")) {
            value = srcMin + value % (srcMax - srcMin);
        } else if (overflow.equals("mirror")) {
            float rest = value % (srcMax - srcMin);
            if ((int) (value / (srcMax - srcMin)) % 2 == 1)
                value = srcMax - rest;
            else
                value = srcMin + rest;
        } else if (overflow.equals("clamp")) {
            value = clamp(value, srcMin, srcMax);
        } else {
            // ignore
        }
        // Convert value to target range.
        value = targetMin + value * (targetMax - targetMin);
        pOutputValue.set(value);
    }
}
