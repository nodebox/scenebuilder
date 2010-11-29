package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Point;
import nodebox.graphics.Transform;
import nodebox.node.*;
import processing.core.PGraphics;

@Description("Places geometry on the points of the template geometry.")
@Drawable
@Category("Geometry")
public class PlaceNode extends Network {

    public final GeometryPort pTemplate = new GeometryPort(this, "template", Port.Direction.INPUT);
    public final GeometryPort pOutput = new GeometryPort(this, "outputGeometry", Port.Direction.OUTPUT);

    public static final String KEY_POINTS = "points";
    public static final String KEY_POINT_INDEX = "pointIndex";
    public static final String KEY_POSITION = "position";
    public static final String KEY_POINT_X = "pointX";
    public static final String KEY_POINT_Y = "pointY";

    public PlaceNode() {
        pOutput.setDisplayName("geo");
    }

    @Override
    public void execute(Context context, float time) {
        Node renderedNode = getRenderedNode();
        GeometryPort pGeometry;
        if (renderedNode != null && renderedNode.hasPort("outputGeometry") && renderedNode.getPort("outputGeometry") instanceof GeometryPort)
            pGeometry = (GeometryPort) renderedNode.getPort("outputGeometry");
        else
            pGeometry = null;

        Geometry shape = new Geometry();

        if (pTemplate.get() == null) {
            if (pGeometry == null)
                pOutput.set(null);
            else {
                Geometry g = pGeometry.get();
                pOutput.set(g == null ? null : g.clone());
            }
        } else {
            int index = 0;
            boolean hasOutput = false;

            int pointCount = pTemplate.get().getPointCount();
            float delta = pointCount > 2 ? 1f / (pointCount - 1f) : 1;
            
            for (Point pt : pTemplate.get().getPoints()) {
                float position = index * delta;
                Context childContext = new Context(context);
                childContext.setValueForNodeKey(this, KEY_POINTS, pTemplate.get().getPointCount());
                childContext.setValueForNodeKey(this, KEY_POINT_INDEX, index);
                childContext.setValueForNodeKey(this, KEY_POSITION, position);
                childContext.setValueForNodeKey(this, KEY_POINT_X, pt.x);
                childContext.setValueForNodeKey(this, KEY_POINT_Y, pt.y);
                super.execute(childContext, time);

                if (pGeometry != null && pGeometry.get() != null) {
                    hasOutput = true;
                    Transform t = new Transform();
                    t.translate(pt.x, pt.y);
                    shape.extend(t.map(pGeometry.get().clone()));
                }
                
                index++;
            }

            pOutput.set(hasOutput ? shape : null);
        }
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
}
