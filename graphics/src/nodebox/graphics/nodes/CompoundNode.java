package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Path;
import nodebox.node.*;

import java.awt.Color;

import static nodebox.graphics.nodes.GraphicsSupport.setStyle;

@Description("Add, subtract or intersect geometry.")
@Drawable
@Category("Geometry")
public class CompoundNode extends GeneratorNode {

    public final int OPERATION_UNION = 0;
    public final int OPERATION_DIFFERENCE = 1;
    public final int OPERATION_INTERSECTION = 2;

    public final GeometryPort pGeometry1 = new GeometryPort(this, "geometry1", Port.Direction.INPUT);
    public final GeometryPort pGeometry2 = new GeometryPort(this, "geometry2", Port.Direction.INPUT);
    public IntPort pOperation = new IntPort(this, "operation", Port.Direction.INPUT, OPERATION_UNION);
    public final BooleanPort pInvertDifference = new BooleanPort(this, "invertDifference", Port.Direction.INPUT, false);
    public final ColorPort pFill = new ColorPort(this, "fill", Port.Direction.INPUT, Color.WHITE);
    public final ColorPort pStroke = new ColorPort(this, "stroke", Port.Direction.INPUT, Color.BLACK);
    public final FloatPort pStrokeWeight = new FloatPort(this, "strokeWeight", Port.Direction.INPUT, 1f);

    public CompoundNode() {
        pGeometry1.setDisplayName("geo 1");
        pGeometry2.setDisplayName("geo 2");
        pOperation.addMenuItem(OPERATION_UNION, "Union");
        pOperation.addMenuItem(OPERATION_DIFFERENCE, "Difference");
        pOperation.addMenuItem(OPERATION_INTERSECTION, "Intersection");
    }

    private Path geometryToPath(Geometry geo) {
        Path path = null;
        for (Path p : geo.getPaths()) {
            if (path == null)
                path = p;
            else
                path = path.united(p);
        }
        return path;
    }

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry1.get() == null) return null;
        if (pGeometry2.get() == null) return pGeometry1.get().clone();

        Geometry geo1, geo2;

        if (pInvertDifference.get()) {
            geo1 = pGeometry2.get();
            geo2 = pGeometry1.get();
        } else {
            geo1 = pGeometry1.get();
            geo2 = pGeometry2.get();
        }

        Path path1 = geometryToPath(geo1);
        Path path2 = geometryToPath(geo2);

        Path compound;

        switch (pOperation.get()) {
            case OPERATION_UNION:
                compound = path1.united(path2);
                break;
            case OPERATION_DIFFERENCE:
                compound = path1.subtracted(path2);
                break;
            case OPERATION_INTERSECTION:
                compound = path1.intersected(path2);
                break;
            default:
                compound = null;
                break;
        }

        if (compound == null) return null;

        setStyle(compound, pFill, pStroke, pStrokeWeight);
        return compound.asGeometry();
    }
}
