package nodebox.builtins.math;

import nodebox.node.*;

import static nodebox.util.ProcessingSupport.clamp;

@Description("Contain a value between a given minimum and maximum.")
@Category("Math")
public class Clamp extends Node {

    public FloatPort pInputValue = new FloatPort(this, "inputValue", Port.Direction.INPUT);
    public FloatPort pMinimum = new FloatPort(this, "minimum", Port.Direction.INPUT, 0);
    public FloatPort pMaximum = new FloatPort(this, "maximum", Port.Direction.INPUT, 100);
    public FloatPort pOutputValue = new FloatPort(this, "outputValue", Port.Direction.OUTPUT);

    public Clamp() {
        pInputValue.setDisplayName("value");
        pOutputValue.setDisplayName("value");
    }

    @Override
    public void execute(Context context, float time) {
        float value = clamp(pInputValue.get(), pMinimum.get(), pMaximum.get());
        pOutputValue.set(value);
    }
}
