package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.Context;
import nodebox.node.FloatPort;
import nodebox.node.IntPort;
import nodebox.node.Port;

public class GridNode extends GeneratorNode {

    public final FloatPort pWidth = new FloatPort(this, "width", Port.Direction.INPUT, 300f);
    public final FloatPort pHeight = new FloatPort(this, "height", Port.Direction.INPUT, 300f);
    public final IntPort pRows = new IntPort(this, "rows", Port.Direction.INPUT, 10);
    public final IntPort pColumns = new IntPort(this, "columns", Port.Direction.INPUT, 10);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT);

    @Override
    public Geometry cook(Context context, float time) {
        Path p = new Path();
        p.setFillColor(null);
        float columns = pColumns.get();
        float colSize = 0f;
        float left = 0f;
        if (columns > 1) {
            colSize = pWidth.get() / (columns - 1f);
            left = pX.get() - pWidth.get() / 2f;
        }
        float rows = pRows.get();
        float rowSize = 0f;
        float top = 0f;
        if (rows > 1) {
            rowSize = pHeight.get() / (rows - 1);
            top = pY.get() - pHeight.get() / 2f;
        }
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int colIndex = 0; colIndex < columns; colIndex++) {
                p.addPoint(left + colIndex * colSize, top + rowIndex * rowSize);
            }
        }
        return p.asGeometry();
    }
}
