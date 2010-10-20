package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Point;
import nodebox.graphics.Transform;
import nodebox.node.*;
import java.util.List;

public class PlaceNode extends FilterNode {

    public final GeometryPort pTemplate = new GeometryPort(this, "template", Port.Direction.INPUT);

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;
        if (pTemplate.get() == null) return pGeometry.get().clone();
        Geometry shape = new Geometry();

        for (Point pt : pTemplate.get().getPoints()) {
          Transform t = new Transform();
          t.translate(pt.x, pt.y);
          shape.extend(t.map(pGeometry.get().clone()));
        }
         return shape;
    }
}
