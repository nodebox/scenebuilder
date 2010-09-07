package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.graphics.Text;
import nodebox.node.*;

import java.awt.*;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;

public class TextPathNode extends GeneratorNode {

    public StringPort pText = new StringPort(this, "text", Port.Direction.INPUT, "hello");
    public StringPort pFont = new StringPort(this, "font", Port.Direction.INPUT, "Verdana");
    public FloatPort pSize = new FloatPort(this, "size", Port.Direction.INPUT, 24f);
    public IntPort pAlign = new IntPort(this, "align", Port.Direction.INPUT, 1);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);
    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    @Override
    public Geometry cook(Context context, float time) {
        Text t = new Text(pText.get(), pX.get(), pY.get(), pWidth.get(), pHeight.get());
        t.setFontName(pFont.get());
        t.setFontSize(pSize.get());
        // valueOf requires a correct value: LEFT, CENTER, RIGHT or JUSTIFY. Anything else will
        // make it crash. If users start doing crazy things and change the alignment, at least
        // make sure you catch the error.
        t.setAlign(Text.Align.valueOf("LEFT"));
        Path p = t.getPath();
        setStyle(p, pFill, pStroke, pStrokeWeight);
        return p.asGeometry();
    }
}
