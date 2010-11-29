package nodebox.graphics.nodes;

import nodebox.node.*;

/**
 * The looper variables can be used in a looper network to get information on the current iteration.
 * <p/>
 * For each iteration, the looper variables provide the index, the total amount of executions,
 * and the position (between 0.0-1.0).
 */
@Description("Variables that contain information for every loop execution.")
@Category("Geometry")
public class PlaceVariables extends Node {

    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.OUTPUT);
    public final IntPort pIndex = new IntPort(this, "index", Port.Direction.OUTPUT, 0);
    public final FloatPort pPosition = new FloatPort(this, "position", Port.Direction.OUTPUT, 0f);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.OUTPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.OUTPUT, 0f);

    @Override
    public void execute(Context context, float time) {
        Network network = getNetwork();
        if (network == null) return;
        if (!(network instanceof PlaceNode)) return;
        pAmount.set((Integer) context.getValueForNodeKey(network, PlaceNode.KEY_POINTS));
        pIndex.set((Integer) context.getValueForNodeKey(network, PlaceNode.KEY_POINT_INDEX));
        pPosition.set((Float) context.getValueForNodeKey(network, PlaceNode.KEY_POSITION));
        pX.set((Float) context.getValueForNodeKey(network, PlaceNode.KEY_POINT_X));
        pY.set((Float) context.getValueForNodeKey(network, PlaceNode.KEY_POINT_Y));
    }

}
