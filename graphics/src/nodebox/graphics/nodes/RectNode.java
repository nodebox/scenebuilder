package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.*;

import java.awt.*;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;

@Description("Create rectangles and rounded rectangles.")
@Drawable(true)
@Category("Geometry")
public class RectNode extends GeneratorNode {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 100);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 100);
    public final FloatPort pRx = new FloatPort(this, "rx", Port.Direction.INPUT);
    public final FloatPort pRy = new FloatPort(this, "ry", Port.Direction.INPUT);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        Path p = new Path();
        if (pRx.get() == 0 && pRy.get() == 0) {
            p.rect(pX.get(), pY.get(), pWidth.get(), pHeight.get());
        } else {
            p.roundedRect(pX.get(), pY.get(), pWidth.get(), pHeight.get(), pRx.get(), pRy.get());
        }
        setStyle(p, pFill, pStroke, pStrokeWeight);
        return p.asGeometry();
    }
}
