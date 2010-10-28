package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.*;

@Description("Generates new points evenly spaced along the given geometry.")
@Category("Geometry")
public class ResampleNode extends FilterNode {

    public static final String METHOD_LENGTH = "length";
    public static final String METHOD_AMOUNT = "amount";

    public StringPort pMethod = new StringPort(this, "method", Port.Direction.INPUT, "length");
    public FloatPort pLength = new FloatPort(this, "length", Port.Direction.INPUT, 10f);
    public IntPort pAmount = new IntPort(this, "amount", Port.Direction.INPUT, 10);
    public BooleanPort pPerContour = new BooleanPort(this, "perContour", Port.Direction.INPUT, false);

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;
        if (pMethod.get().equals(METHOD_LENGTH)) {
            float length = Math.max(pLength.get(), 1f);
            return pGeometry.get().resampleByLength(length);
        } else {
            int amount = Math.max(pAmount.get(), 1);
            return pGeometry.get().resampleByAmount(amount, pPerContour.get());
        }
    }
}
