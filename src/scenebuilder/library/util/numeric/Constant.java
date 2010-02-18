package scenebuilder.library.util.numeric;

import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class Constant extends Node {

    private final String PORT_VALUE = "value";
    private final String PORT_NUMBER = "number";
    private double value = 0.0;

    public Constant() {
        super(Function.Generator);
        setAttribute(Node.DISPLAY_NAME_ATTRIBUTE, "Constant Value");
        setAttribute(Node.DESCRIPTION_ATTRIBUTE, "This node stores a constant value in the output port. This can be used for variables for example, where the same value is used in many places.");
        addInputPort(Port.Type.NUMBER, PORT_VALUE, 0.0);
        addOutputPort(Port.Type.NUMBER, PORT_NUMBER);
    }

    @Override
    public boolean execute(Context context, double time) {
        // Since this is a constant, time does not have an effect on this node.
        setValue(PORT_NUMBER, asNumber(PORT_VALUE));
        return true;
    }

}
