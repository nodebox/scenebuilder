package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.Context;
import nodebox.node.DrawingNode;
import nodebox.node.Port;
import processing.core.PGraphics;

public class DrawGeometry extends DrawingNode {

    public GeometryPort pGeometry = new GeometryPort(this, "geometry", Port.Direction.INPUT);

    @Override
    public void draw(PGraphics g, Context context, float time) {
        Geometry geo = pGeometry.get();
        if (geo == null) return;
        geo.draw(g);
    }

}
