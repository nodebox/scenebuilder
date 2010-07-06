package scenebuilder.library.util;

import scenebuilder.model.Context;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class IteratorVariables extends Node {
    public static final String PORT_AMOUNT = "amount";
    public static final String PORT_INDEX = "index";
    public static final String PORT_POSITION = "position";

    public IteratorVariables() {
        setDisplayName("Iterator Variables");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Per-instance variables that contain state information for every single execution.");
        addOutputPort(Port.Type.INTEGER, PORT_AMOUNT);
        addOutputPort(Port.Type.INTEGER, PORT_INDEX);
        addOutputPort(Port.Type.NUMBER, PORT_POSITION);
    }

    @Override
    public boolean execute(Context context, double time) {
        Macro macro = getParent();
        if (macro == null) return true;
        if (!(macro instanceof Iterator)) return true;
        Iterator iterator = (Iterator) macro;
        setValue(PORT_AMOUNT, context.getValueForNodeKey(iterator, Iterator.KEY_AMOUNT));
        setValue(PORT_INDEX, context.getValueForNodeKey(iterator, Iterator.KEY_INDEX));
        setValue(PORT_POSITION, context.getValueForNodeKey(iterator, Iterator.KEY_POSITION));
        return true;
    }
}
