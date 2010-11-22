package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.*;

import java.awt.*;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;

@Description("Create a star shape.")
@Drawable
@Category("Geometry")
public class StarNode extends GeneratorNode {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final IntPort pPoints = new IntPort(this, "points", Port.Direction.INPUT, 20);
    public final FloatPort pOuter = new FloatPort(this, "outer", Port.Direction.INPUT, 200);
    public final FloatPort pInner = new FloatPort(this, "inner", Port.Direction.INPUT, 100);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        Path p = new Path();
        p.moveto(pX.get(), pY.get() + pOuter.get() / 2);
        
        // Calculate the points of the star.
        for (int i=1; i < pPoints.get() * 2; i++) {
            float angle = i * (float) Math.PI / pPoints.get();
            float x = (float) Math.sin(angle);
            float y = (float) Math.cos(angle);
            float radius = i % 2 == 1 ? pInner.get() / 2 : pOuter.get() / 2;
            x = pX.get() + radius * x;
            y = pY.get() + radius * y;
            p.lineto(x, y);
        }
        p.close();
        setStyle(p, pFill, pStroke, pStrokeWeight);
        return p.asGeometry();
    }
}
