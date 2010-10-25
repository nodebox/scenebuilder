package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.graphics.Transform;
import nodebox.node.*;
import nodebox.node.Network;
import processing.core.PGraphics;

@Description("Create multiple copies of a shape.")
public class CopyNode extends Network {

    public final IntPort pCopies = new IntPort(this, "copies", Port.Direction.INPUT, 1);
    public final StringPort pOrder = new StringPort(this, "order", Port.Direction.INPUT, "tsr");
    public final FloatPort pTx = new FloatPort(this, "translate x", Port.Direction.INPUT);
    public final FloatPort pTy = new FloatPort(this, "translate y", Port.Direction.INPUT);
    public final FloatPort pR = new FloatPort(this, "rotate", Port.Direction.INPUT);
    public final FloatPort pSx = new FloatPort(this, "scale x", Port.Direction.INPUT);
    public final FloatPort pSy = new FloatPort(this, "scale y", Port.Direction.INPUT);
    public final GeometryPort pOutput = new GeometryPort(this, "output", Port.Direction.OUTPUT);

    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INDEX = "index";
    public static final String KEY_POSITION = "position";

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
        String order = pOrder.get();

        GeometryPort pGeometry = getGeometryOutputPort(getRenderedNode());
        Geometry g = new Geometry();
        boolean hasOutput = false;

        for (int i = 0; i < copies; i++) {
            float position = i / (float) copies;
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
