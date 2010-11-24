package nodebox.builtins.math;

import nodebox.node.*;

import static nodebox.util.ProcessingSupport.clamp;

@Description("Given a value and its range, convert the value to the new range.")
@Category("Math")
public class ConvertRange extends Node {

    public final int OVERFLOW_IGNORE = 0;
    public final int OVERFLOW_WRAP = 1;
    public final int OVERFLOW_MIRROR = 2;
    public final int OVERFLOW_CLAMP = 3;

    public FloatPort pInputValue = new FloatPort(this, "inputValue", Port.Direction.INPUT);
    public FloatPort pSourceMinimum = new FloatPort(this, "sourceMinimum", Port.Direction.INPUT, 0);
    public FloatPort pSourceMaximum = new FloatPort(this, "sourceMaximum", Port.Direction.INPUT, 100);
    public FloatPort pTargetMinimum = new FloatPort(this, "targetMinimum", Port.Direction.INPUT, 0);
    public FloatPort pTargetMaximum = new FloatPort(this, "targetMaximum", Port.Direction.INPUT, 255);
    public IntPort pOverflow = new IntPort(this, "overflow", Port.Direction.INPUT, OVERFLOW_CLAMP);
    public FloatPort pOutputValue = new FloatPort(this, "outputValue", Port.Direction.OUTPUT);

    public ConvertRange() {
        pInputValue.setDisplayName("value");
        pOutputValue.setDisplayName("value");
        pOverflow.addMenuItem(OVERFLOW_IGNORE, "Ignore");
        pOverflow.addMenuItem(OVERFLOW_WRAP, "Wrap");
        pOverflow.addMenuItem(OVERFLOW_MIRROR, "Mirror");
        pOverflow.addMenuItem(OVERFLOW_CLAMP, "Clamp");
    }

    @Override
    public void execute(Context context, float time) {
        float srcMin = pSourceMinimum.get();
        float srcMax = pSourceMaximum.get();
        float targetMin = pTargetMinimum.get();
        float targetMax = pTargetMaximum.get();
        int overflow = pOverflow.get();
        float value = pInputValue.get();
        switch (overflow) {
            case OVERFLOW_WRAP:
                value = srcMin + value % (srcMax - srcMin);
                break;
            case OVERFLOW_MIRROR:
                float rest = value % (srcMax - srcMin);
                if ((int) (value / (srcMax - srcMin)) % 2 == 1)
                    value = srcMax - rest;
                else
                    value = srcMin + rest;
                break;
            case OVERFLOW_CLAMP:
                value = clamp(value, srcMin, srcMax);
                break;
        }

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
