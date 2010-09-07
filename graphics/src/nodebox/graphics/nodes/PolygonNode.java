package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.*;

import java.awt.*;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;
import static nodebox.util.ProcessingSupport.angle;
import static nodebox.util.ProcessingSupport.coordinates;

public class PolygonNode extends GeneratorNode {

    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pRadius = new FloatPort(this, "radius", Port.Direction.INPUT, 100);
    public final IntPort pSides = new IntPort(this, "sides", Port.Direction.INPUT, 3);
    public final BooleanPort pAlign = new BooleanPort(this, "align", Port.Direction.INPUT, false);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        Path p = new Path();
        float x = pX.get();
        float y = pY.get();
        float r = pRadius.get();
        int sides = Math.max(pSides.get(), 3);
        float a = 360f / sides;
        float da = 0;
        if (pAlign.get()) {
            double[] c0 = coordinates(x, y, r, 0);
            double[] c1 = coordinates(x, y, r, a);
            da = (float) -angle(c1[0], c1[1], c0[0], c0[1]);
        }
        for (int i=0;i<sides;i++) {
            double[] c1 = coordinates(x, y, r, (a*i) + da);
            p.addPoint((float)c1[0], (float)c1[1]);
        }
        p.close();
        setStyle(p, pFill, pStroke, pStrokeWeight);
        return p.asGeometry();
    }
}
