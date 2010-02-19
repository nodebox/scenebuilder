package scenebuilder.library.render;

import org.lwjgl.opengl.GL11;
import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;
import scenebuilder.util.GLUtils;

import java.awt.*;

public class Clear extends Node {

    public static final String PORT_ENABLE = "enable";
    public static final String PORT_COLOR = "color";

    public Clear() {
        super(Function.Renderer);
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Clear");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Fill the display with a constant color.");
        addInputPort(Port.Type.BOOLEAN, PORT_ENABLE, true);
        addInputPort(Port.Type.COLOR, PORT_COLOR, Color.GRAY);
    }

    @Override
    public boolean execute(Context context, double time) {
        if (!asBoolean(PORT_ENABLE)) return true;
        GLUtils.setClearColor(asColor(PORT_COLOR));
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        return true;
    }
}
