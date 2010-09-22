package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.*;

import java.awt.Color;
import static nodebox.graphics.nodes.GraphicsSupport.setStyle;

@Description("Change the color of a shape.")
public class ColorNode extends FilterNode {

    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;
        Geometry newShape = pGeometry.get().clone();
        setStyle(newShape, pFill, pStroke, pStrokeWeight);
        return newShape;
    }
}
