package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

@Description("Define the order in which input nodes are executed.")
public class Sequence extends DrawingNode {

    private List<BooleanPort> stepPorts = new ArrayList<BooleanPort>();

    public Sequence() {
        createLayer();
        createLayer();
        createLayer();
    }

    public BooleanPort createLayer() {
        String stepName = "step" + (stepPorts.size() + 1);
        BooleanPort port = new BooleanPort(this, stepName, Port.Direction.INPUT);
        stepPorts.add(port);
        return port;
    }

    @Override
    public void updateDependencies(Context context, float time) {
        // Overridden so that the dependencies do not update themselves and draw something.
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        for (BooleanPort port : stepPorts) {
            Connection c = port.getConnection();
            if (c != null) {
                c.getOutputNode().update(context, time);
            }
        }
    }

}
