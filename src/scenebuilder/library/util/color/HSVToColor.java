package scenebuilder.library.util.color;

import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

import java.awt.*;

public class HSVToColor extends Node {

    public static final String PORT_H = "h";
    public static final String PORT_S = "s";
    public static final String PORT_V = "v";
    public static final String PORT_A = "a";
    public static final String PORT_COLOR = "color";

    public HSVToColor() {
        super(Function.Filter);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "HSV to Color");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Generate a color from hue, saturation and value components.");
        addInputPort(Port.Type.NUMBER, PORT_H, 1.0);
        addInputPort(Port.Type.NUMBER, PORT_S, 1.0);
        addInputPort(Port.Type.NUMBER, PORT_V, 1.0);
        addInputPort(Port.Type.NUMBER, PORT_A, 1.0);
        addOutputPort(Port.Type.COLOR, PORT_COLOR);
    }

    private float clamp(String portName) {
        double v = asNumber(portName);
        return v > 1.0 ? 1f : v < 0.0 ? 0f : (float) v;
    }

    @Override
    public boolean execute(Context context, double time) {
        // TODO: Support for alpha.
        Color c = new Color(Color.HSBtoRGB(clamp(PORT_H), clamp(PORT_S), clamp(PORT_V)));
        setValue(PORT_COLOR, c);
        return true;
    }
}
