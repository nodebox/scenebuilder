package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Transform;
import nodebox.node.*;

@Description("Transform the location, rotation and scale of a shape.")
@Category("Geometry")
public class TransformNode extends FilterNode {

    public final StringPort pOrder = new StringPort(this, "order", Port.Direction.INPUT, "trs");
    public final FloatPort pTx = new FloatPort(this, "tx", Port.Direction.INPUT);
    public final FloatPort pTy = new FloatPort(this, "ty", Port.Direction.INPUT);
    public final FloatPort pR = new FloatPort(this, "r", Port.Direction.INPUT);
    public final FloatPort pSx = new FloatPort(this, "sx", Port.Direction.INPUT, 100);
    public final FloatPort pSy = new FloatPort(this, "sy", Port.Direction.INPUT, 100);

    @Override
    public Geometry cook(Context context, float time) {
        if (pGeometry.get() == null) return null;

        Transform t = new Transform();
        // Each letter of the order describes an operation.
        for (int i = 0; i < pOrder.get().length(); i++) {
            char op = pOrder.get().charAt(i);
            switch (op) {
                case 't':
                    t.translate(pTx.get(), pTy.get());
                    break;
                case 'r':
                    t.rotate(pR.get());
                    break;
                case 's':
                    t.scale(pSx.get() / 100.0, pSy.get() / 100.0);
                    break;
                default:
                    break;
            }
        }
        // Transform.map clones and transforms the geometry.
        return t.map(pGeometry.get());

    }
}
