package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Description("Layer the different drawing layers")
public class Layers extends DrawingNode {

    private BooleanPort pClear = new BooleanPort(this, "clear", Port.Direction.INPUT, false);
    private ColorPort pClearColor = new ColorPort(this, "clearColor", Port.Direction.INPUT, Color.LIGHT_GRAY);
    private List<BooleanPort> layerPorts = new ArrayList<BooleanPort>();

    public Layers() {
        createLayer();
        createLayer();
        createLayer();
    }

    public BooleanPort createLayer() {
        String layerName = "layer" + (layerPorts.size() + 1);
        BooleanPort port = new BooleanPort(this, layerName, Port.Direction.INPUT);
        layerPorts.add(port);
        return port;
    }

    @Override
    public void updateDependencies(Context context, float time) {
        // Overridden so that the dependencies do not update themselves and draw something.
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        if (pClear.get()) {
            Color c = pClearColor.get();
            g.background(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
        for (BooleanPort layerPort : layerPorts) {
            Connection c = layerPort.getConnection();
            if (c != null) {
                c.getOutputNode().update(context, time);
            }
        }
    }

}
