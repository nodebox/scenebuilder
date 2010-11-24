package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Transform;
import nodebox.node.*;
import nodebox.node.Network;
import processing.core.PGraphics;

@Description("Create multiple copies of a shape.")
@Drawable
@Category("Geometry")
public class CopyNode extends Network {

    public final String[] orderings = {"srt", "str", "rst", "rts", "tsr", "trs"};
    public final int ORDER_SCALE_ROT_TRANS = 0;
    public final int ORDER_SCALE_TRANS_ROT = 1;
    public final int ORDER_ROT_SCALE_TRANS = 2;
    public final int ORDER_ROT_TRANS_SCALE = 3;
    public final int ORDER_TRANS_SCALE_ROT = 4;
    public final int ORDER_TRANS_ROT_SCALE = 5;

    public final IntPort pCopies = new IntPort(this, "copies", Port.Direction.INPUT, 1);
    public final IntPort pOrder = new IntPort(this, "order", Port.Direction.INPUT, ORDER_TRANS_SCALE_ROT);
    public final FloatPort pTx = new FloatPort(this, "translateX", Port.Direction.INPUT);
    public final FloatPort pTy = new FloatPort(this, "translateY", Port.Direction.INPUT);
    public final FloatPort pR = new FloatPort(this, "rotate", Port.Direction.INPUT);
    public final FloatPort pSx = new FloatPort(this, "scaleX", Port.Direction.INPUT);
    public final FloatPort pSy = new FloatPort(this, "scaleY", Port.Direction.INPUT);
    public final GeometryPort pOutput = new GeometryPort(this, "output", Port.Direction.OUTPUT);

    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INDEX = "index";
    public static final String KEY_POSITION = "position";

    public CopyNode() {
        pOrder.addMenuItem(ORDER_SCALE_ROT_TRANS, "Scale Rot Trans");
        pOrder.addMenuItem(ORDER_SCALE_TRANS_ROT, "Scale Trans Rot");
        pOrder.addMenuItem(ORDER_ROT_SCALE_TRANS, "Rot Scale Trans");
        pOrder.addMenuItem(ORDER_ROT_TRANS_SCALE, "Rot Trans Scale");
        pOrder.addMenuItem(ORDER_TRANS_SCALE_ROT, "Trans Scale Rot");
        pOrder.addMenuItem(ORDER_TRANS_ROT_SCALE, "Trans Rot Scale");
    }

    public GeometryPort getGeometryOutputPort(Node node) {
        if (node == null) return null;

        for (Port port : node.getPorts()) {
            if (port.isOutputPort() && port instanceof GeometryPort)
                return (GeometryPort) port;
        }
        
        return null;
    }

    @Override
    public void execute(Context context, float time) {
        int copies = pCopies.get();
        float tx = pTx.get();
        float ty = pTy.get();
        float r = pR.get();
        float sx = pSx.get();
        float sy = pSy.get();
        String order = orderings[pOrder.get()];

        float delta = copies > 2 ? 1f / (copies - 1f) : 1;

        GeometryPort pGeometry = getGeometryOutputPort(getRenderedNode());
        Geometry g = new Geometry();
        boolean hasOutput = false;

        for (int i = 0; i < copies; i++) {
            float position = i * delta;
            Context childContext = new Context(context);
            childContext.setValueForNodeKey(this, KEY_AMOUNT, copies);
            childContext.setValueForNodeKey(this, KEY_INDEX, i);
            childContext.setValueForNodeKey(this, KEY_POSITION, position);
            super.execute(childContext, time);

            if (pGeometry != null && pGeometry.get() != null) {
                hasOutput = true;
                Transform t = new Transform();

                for (int j = 0; j < order.length(); j++) {
                    switch (order.charAt(j)) {
                        case 't':
                            t.translate(tx * i, ty * i);
                            break;
                        case 'r':
                            t.rotate(r * i);
                            break;
                        case 's':
                            t.scale(1 + (sx * i / 100), 1 + (sy * i / 100));
                            break;
                        default:
                            break;
                    }
                }

                g.extend(t.map(pGeometry.get()));
            }
        }
        pOutput.set(hasOutput ? g : null);
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        Geometry geo = pOutput.get();
        if (geo == null) return;
        g.pushMatrix();
        g.translate(g.width / 2f, g.height / 2f);
        geo.draw(g);
        g.popMatrix();
    }
}
