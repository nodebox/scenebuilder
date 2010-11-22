package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.*;

import java.awt.*;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;

@Description("Generate ellipses and circles.")
@Drawable
@Category("Geometry")
public class EllipseNode extends GeneratorNode {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 100);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 100);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        Path p = new Path();
        p.ellipse(pX.get(), pY.get(), pWidth.get(), pHeight.get());
        setStyle(p, pFill, pStroke, pStrokeWeight);
        return p.asGeometry();
    }
}
