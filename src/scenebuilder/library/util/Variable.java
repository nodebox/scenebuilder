package scenebuilder.library.util;

import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class Variable extends Node {

    public static final String PORT_INPUT = "input";
    public static final String PORT_OUTPUT = "output";


    public static Variable numericVariable(String displayName) {
        Variable variable = new Variable();
        Port p = variable.addInputPort(Port.Type.NUMBER, PORT_INPUT, 0.0);
        p.setAttribute(Port.DISPLAY_NAME_ATTRIBUTE, displayName);
        variable.addOutputPort(Port.Type.NUMBER, PORT_OUTPUT);
        return variable;
    }

    private Variable() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Variable");
        setAttribute(DESCRIPTION_ATTRIBUTE, "A utility node that allows you to re-use a value multiple times.");
    }

    @Override
    public boolean execute(Context context, double time) {
        setValue(PORT_OUTPUT, getValue(PORT_INPUT));
        return true;
    }
}
