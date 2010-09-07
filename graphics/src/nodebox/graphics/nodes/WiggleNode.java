package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Point;
import nodebox.node.*;

import java.util.Random;

@Description("Shift points by a random amount.")
public class WiggleNode extends FilterNode {

    public final FloatPort pWx = new FloatPort(this, "wx", Port.Direction.INPUT, 10);
    public final FloatPort pWy = new FloatPort(this, "wy", Port.Direction.INPUT, 10);
    public final IntPort pSeed = new IntPort(this, "seed", Port.Direction.INPUT, 1);

    @Override
    public Geometry cook(Context context, float time) {
        Random random = new Random(pSeed.get());
        if (pGeometry.get() == null) return null;
        Geometry newShape = pGeometry.get().clone();
        float wx = pWx.get();
        float wy = pWy.get();
        for (Point pt : newShape.getPoints()) {
            float dx = (random.nextFloat() - 0.5f) * wx * 2f;
            float dy = (random.nextFloat() - 0.5f) * wy * 2f;
            pt.x += dx;
            pt.y += dy;
        }
        return newShape;
    }
}
