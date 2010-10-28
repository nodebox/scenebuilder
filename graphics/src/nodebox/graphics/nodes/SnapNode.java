package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Point;
import nodebox.node.*;

@Description("Snap geometry to a grid.")
@Category("Geometry")
public class SnapNode extends FilterNode {

    public FloatPort pDistance = new FloatPort(this, "distance", Port.Direction.INPUT, 10f);
    public FloatPort pStrength = new FloatPort(this, "strength", Port.Direction.INPUT, 100f);
    public FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);

    public static float snap(float v, float distance, float strength) {
        return (v * (1f - strength)) + (strength * Math.round(v / distance) * distance);
    }

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;
        Geometry newGeometry = pGeometry.get().clone();
        float distance = pDistance.get();
        float strength = pStrength.get() / 100f;
        float x = pX.get();
        float y = pY.get();
        for (Point pt : newGeometry.getPoints()) {
            pt.x = snap(pt.x + x, distance, strength) - x;
            pt.y = snap(pt.y + y, distance, strength) - y;
        }
        return newGeometry;
    }
}
