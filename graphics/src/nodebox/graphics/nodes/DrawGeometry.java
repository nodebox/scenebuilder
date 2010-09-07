package nodebox.graphics.nodes;

import nodebox.graphics.Contour;
import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.graphics.Point;
import nodebox.node.Context;
import nodebox.node.DrawingNode;
import nodebox.node.Port;
import processing.core.PApplet;
import processing.core.PConstants;

import static nodebox.util.ProcessingSupport.setStyle;

public class DrawGeometry extends DrawingNode {

    public GeometryPort pGeometry = new GeometryPort(this, "geometry", Port.Direction.INPUT);

    @Override
    public void draw(PApplet g, Context context, float time) {
        Geometry geo = pGeometry.get();
        if (geo == null) return;
        for (Path path : geo.getPaths()) {
            setStyle(g, path.getFill().getAwtColor(), path.getStroke().getAwtColor(), path.getStrokeWidth());
            for (Contour c : path.getContours()) {
                g.beginShape(PConstants.POLYGON);
                for (Point p : c.getPoints()) {
                    g.vertex(p.x, p.y);
                }
                g.endShape(PConstants.CLOSE);
            }
        }
    }

}
