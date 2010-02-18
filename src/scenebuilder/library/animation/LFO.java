package scenebuilder.library.animation;

import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class LFO extends Node {

    public static final String PORT_PERIOD = "period";
    public static final String PORT_PHASE = "phase";
    public static final String PORT_AMPLITUDE = "amplitude";
    public static final String PORT_OFFSET = "offset";
    public static final String PORT_RESULT = "result";


    public LFO() {
        super(Function.Generator);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Low Frequency Oscillator");
        setAttribute(DESCRIPTION_ATTRIBUTE, "A low frequency oscillator");
        addInputPort(Port.Type.NUMBER, PORT_PERIOD, 1.0);
        addInputPort(Port.Type.NUMBER, PORT_PHASE, 0.0);
        addInputPort(Port.Type.NUMBER, PORT_AMPLITUDE, 50.0);
        addInputPort(Port.Type.NUMBER, PORT_OFFSET, 0.0);
        addOutputPort(Port.Type.NUMBER, PORT_RESULT);
    }

    @Override
    public boolean execute(Context context, double time) {
        double period = asNumber(PORT_PERIOD);
        double v;
        if (period != 0.0) {
            // TODO: Need to make sure that 1 second ==  1 period. Use Math.PI.
            v = Math.sin(asNumber(PORT_PHASE) + time / period) * asNumber(PORT_AMPLITUDE) + asNumber(PORT_OFFSET);
        } else {
            v = 0.0;
        }
        setValue(PORT_RESULT, v);
        return true;
    }
}
