package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.graphics.Point;
import nodebox.graphics.Rect;
import nodebox.node.*;

import java.util.Random;

@Description("Generate points within the boundaries of a shape.")
public class ScatterNode extends FilterNode {

    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.INPUT, 20);
    public final IntPort pSeed = new IntPort(this, "seed", Port.Direction.INPUT, 1);

    @Override
    public Geometry cook(Context context, float time) {
        Random random = new Random(pSeed.get());
        if (pGeometry.get() == null) return null;
        Geometry shape = pGeometry.get();
        Rect bounds = shape.getBounds();
        float bx = bounds.getX();
        float by = bounds.getY();
        float bw = bounds.getWidth();
        float bh = bounds.getHeight();
        Path p = new Path();
        for (int i=0; i < pAmount.get(); i++) {
            int tries = 100;
            while (tries > 0) {
                Point pt = new Point(bx + random.nextFloat() * bw, by + random.nextFloat() * bh);
                if (shape.contains(pt)) {
                    p.moveto(pt.x, pt.y);
                    break;
                }
                tries--;
            }
        }
        return p.asGeometry();
    }
}
