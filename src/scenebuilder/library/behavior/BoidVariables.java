package scenebuilder.library.behavior;

import scenebuilder.model.Context;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class BoidVariables extends Node {

    public static final String PORT_AMOUNT = "amount";
    public static final String PORT_INDEX = "index";
    public static final String PORT_X = "x";
    public static final String PORT_Y = "y";
    public static final String PORT_Z = "z";
    public static final String PORT_VX = "vx";
    public static final String PORT_VY = "vy";
    public static final String PORT_VZ = "vz";

    public BoidVariables() {
        setDisplayName("Boid Variables");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Per-instance variables that contain state information for a single boid such as its position and velocity.");
        addOutputPort(Port.Type.INTEGER, PORT_AMOUNT);
        addOutputPort(Port.Type.INTEGER, PORT_INDEX);
        addOutputPort(Port.Type.NUMBER, PORT_X);
        addOutputPort(Port.Type.NUMBER, PORT_Y);
        addOutputPort(Port.Type.NUMBER, PORT_Z);
        addOutputPort(Port.Type.NUMBER, PORT_VX);
        addOutputPort(Port.Type.NUMBER, PORT_VY);
        addOutputPort(Port.Type.NUMBER, PORT_VZ);
    }

    @Override
    public boolean execute(Context context, double time) {
        Macro macro = getParent();
        if (macro == null) return true;
        if (!(macro instanceof Boids)) return true;
        Boids boids = (Boids) macro;
        setValue(PORT_AMOUNT, context.getValueForNodeKey(boids, Boids.KEY_AMOUNT));
        setValue(PORT_INDEX, context.getValueForNodeKey(boids, Boids.KEY_INDEX));
        setValue(PORT_X, context.getValueForNodeKey(boids, Boids.KEY_X));
        setValue(PORT_Y, context.getValueForNodeKey(boids, Boids.KEY_Y));
        setValue(PORT_Z, context.getValueForNodeKey(boids, Boids.KEY_Z));
        setValue(PORT_VX, context.getValueForNodeKey(boids, Boids.KEY_VX));
        setValue(PORT_VY, context.getValueForNodeKey(boids, Boids.KEY_VY));
        setValue(PORT_VZ, context.getValueForNodeKey(boids, Boids.KEY_VZ));
        return true;
    }


}
