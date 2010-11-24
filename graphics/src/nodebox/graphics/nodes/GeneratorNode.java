package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.Context;
import nodebox.node.Node;
import nodebox.node.Port;
import processing.core.PGraphics;

public abstract class GeneratorNode extends Node {

    public final GeometryPort pOutput = new GeometryPort(this, "outputGeometry", Port.Direction.OUTPUT);

    public GeneratorNode() {
        pOutput.setDisplayName("geo");
    }

    @Override
    public void execute(Context context, float time) {
        Geometry geometry = cook(context, time);
        pOutput.set(geometry);
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        Geometry geo = pOutput.get();
        if (geo == null) return;
        g.pushMatrix();
        g.translate(g.width / 2f, g.height / 2f);
        geo.draw(g);
        g.popMatrix();
    }

    public abstract Geometry cook(Context context, float time);
}
