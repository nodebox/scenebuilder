package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Rect;
import nodebox.graphics.Transform;
import nodebox.node.*;

@Description("Fit a shape within bounds.")
@Drawable
@Category("Geometry")
public class FitNode extends FilterNode {

    public final GeometryPort pTemplate = new GeometryPort(this, "template", Port.Direction.INPUT);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 300f);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 300f);
    public final BooleanPort pKeepProportions = new BooleanPort(this, "keepProportions", Port.Direction.INPUT);

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;

        Rect bounds = pGeometry.get().getBounds();

        float px, py, pw, ph;
        px = bounds.getX();
        py = bounds.getY();
        pw = bounds.getWidth();
        ph = bounds.getHeight();

        // Make sure pw and ph aren't infinitely small numbers.
        // This will lead to incorrect transformations with for examples lines.
        if (0 < pw && pw <= 0.000000000001)
            pw = 0;
        if (0 < ph && ph <= 0.000000000001)
            ph = 0;

        // if a template shape is given, use its bounding rectangle,
        // otherwise use the input values.
        float x, y, width, height;
        if (pTemplate.get() != null) {
            Rect tBounds = pTemplate.get().getBounds();
            width = tBounds.getWidth();
            height = tBounds.getHeight();
            x = tBounds.getX() + width / 2;
            y = tBounds.getY() + height / 2;
        } else {
            x = pX.get();
            y = pY.get();
            width = pWidth.get();
            height = pHeight.get();
        }

        Transform t = new Transform();
        t.translate(x, y);

        if (pKeepProportions.get()) {
            // Don't scale widths or heights that are equal to zero.
            float w = pw == 0 ? Float.POSITIVE_INFINITY : width / pw;
            float h = ph == 0 ? Float.POSITIVE_INFINITY : height / ph;
            t.scale(Math.min(w, h));
        } else {
            // Don't scale widths or heights that are equal to zero.
            float w = pw == 0 ? 1 : width / pw;
            float h = ph == 0 ? 1 : height / ph;
            t.scale(w, h);
        }

        t.translate(-pw / 2 - px, -ph / 2 - py);

        return t.map(pGeometry.get());
    }
}
