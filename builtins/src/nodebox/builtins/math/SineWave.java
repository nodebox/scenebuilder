package nodebox.builtins.math;

import nodebox.node.*;

import static nodebox.util.ProcessingSupport.clamp;

@Description("A sine wave generator")
public class SineWave extends Node {

    public FloatPort pPosition = new FloatPort(this, "position", Port.Direction.INPUT);
    public FloatPort pAmplitude = new FloatPort(this, "amplitude", Port.Direction.INPUT, 100f);
    public FloatPort pPhase = new FloatPort(this, "phase", Port.Direction.INPUT, 1f);
    public FloatPort pOffset = new FloatPort(this, "offset", Port.Direction.INPUT);
    public FloatPort pValue= new FloatPort(this, "value", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        float phase = clamp(pPhase.get(), 0.1f, 100000f);
        float value = (float) (Math.sin(pPosition.get() / phase) * pAmplitude.get() + pOffset.get());
        pValue.set(value);
    }

}
