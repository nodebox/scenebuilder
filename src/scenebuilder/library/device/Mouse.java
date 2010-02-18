package scenebuilder.library.device;

import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class Mouse extends Node {

    public static final String PORT_X = "x";
    public static final String PORT_Y = "y";
    public static final String PORT_LEFT = "left";
    public static final String PORT_RIGHT = "right";
    public static final String PORT_X_SCROLL = "xScroll";
    public static final String PORT_Y_SCROLL = "yScroll";


    public Mouse() {
        super(Function.Generator);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Mouse");
        setAttribute(DESCRIPTION_ATTRIBUTE,"Read the current state of the mouse." );
        addOutputPort(Port.Type.NUMBER, PORT_X);
        addOutputPort(Port.Type.NUMBER, PORT_Y);
        addOutputPort(Port.Type.BOOLEAN, PORT_LEFT);
        addOutputPort(Port.Type.BOOLEAN, PORT_RIGHT);
        addOutputPort(Port.Type.NUMBER, PORT_X_SCROLL);
        addOutputPort(Port.Type.NUMBER, PORT_Y_SCROLL);
    }

    @Override
    public boolean execute(Context context, double time) {
        setValue(PORT_X, (double)context.getMouseX());
        setValue(PORT_Y, (double)context.getMouseY());

        return true;
    }
}
