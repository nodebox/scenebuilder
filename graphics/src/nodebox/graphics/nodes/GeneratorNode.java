package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.Context;
import nodebox.node.Node;
import nodebox.node.Port;

public abstract class GeneratorNode extends Node {

    public final GeometryPort pOutput = new GeometryPort(this, "output", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        Geometry geometry = cook(context, time);
        pOutput.set(geometry);
    }

    public abstract Geometry cook(Context context, float time);
}
