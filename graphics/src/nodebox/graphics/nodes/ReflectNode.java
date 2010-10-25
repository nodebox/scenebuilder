package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Point;
import nodebox.node.*;

import static nodebox.util.ProcessingSupport.angle;
import static nodebox.util.ProcessingSupport.coordinates;
import static nodebox.util.ProcessingSupport.distance;
import static nodebox.util.ProcessingSupport.radians;


@Description("Mirrors and copies geometry across an invisible axis.")
public class ReflectNode extends FilterNode {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 50);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pAngle = new FloatPort(this, "angle", Port.Direction.INPUT, 120);
    public final BooleanPort pKeepOriginal = new BooleanPort(this, "keep original", Port.Direction.INPUT, true);

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;

        float x0 = pX.get();
        float y0 = pY.get();
        float a0 = pAngle.get();

        Geometry newGeometry = new Geometry();
        Geometry shape = pGeometry.get().clone();
        if (pKeepOriginal.get())
            newGeometry.extend(shape);

        for (Point pt : shape.getPoints() ) {
            float px = pt.x;
            float py = pt.y;
            double d = distance(px, py, x0, y0);
            double a = angle(px, py, x0, y0);
            double[] c0 = coordinates(x0, y0, d * Math.cos(radians(a - a0)), 180 + a0);
            double x = c0[0];
            double y = c0[1];
            d = distance(px, py, x, y);
            a = angle(px, py, x, y);
            c0 = coordinates(px, py, d * 2, a);
            pt.x = (float) c0[0];
            pt.y = (float) c0[1];
        }

        newGeometry.extend(shape);
        return newGeometry;
    }
}
