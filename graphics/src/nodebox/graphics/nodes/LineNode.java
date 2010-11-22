package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.*;

import java.awt.*;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;
import static nodebox.util.ProcessingSupport.coordinates;

@Description("Generates a line based on a starting point and angle/distance.")
@Drawable(true)
@Category("Geometry")
public class LineNode extends GeneratorNode {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pAngle = new FloatPort(this, "angle", Port.Direction.INPUT);
    public final FloatPort pDistance = new FloatPort(this, "distance", Port.Direction.INPUT);
    public final IntPort pPoints = new IntPort(this, "points", Port.Direction.INPUT, 2);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        Path p = new Path();
        double[] coords = coordinates(pX.get(), pY.get(), pDistance.get(), pAngle.get());
        p.line(pX.get(), pY.get(), (float) coords[0], (float) coords[1]);
        setStyle(p, null, pStroke, pStrokeWeight);
        if (pPoints.get() > 2) {
            p = p.resampleByAmount(pPoints.get(), true);
        }
        return p.asGeometry();
    }
}
