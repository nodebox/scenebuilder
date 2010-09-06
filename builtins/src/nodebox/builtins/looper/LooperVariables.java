package nodebox.builtins.looper;

import nodebox.node.*;

/**
 * The looper variables can be used in a looper network to get information on the current iteration.
 * <p/>
 * For each iteration, the looper variables provide the index, the total amount of executions,
 * and the position (between 0.0-1.0).
 */
@Description("Variables that contain information for every loop execution.")
public class LooperVariables extends Node {

    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.OUTPUT);
    public final IntPort pIndex = new IntPort(this, "index", Port.Direction.OUTPUT, 0);
    public final FloatPort pPosition = new FloatPort(this, "position", Port.Direction.OUTPUT, 0f);

    @Override
    public void execute(Context context, double time) {
        Network network = getNetwork();
        if (network == null) return;
        if (!(network instanceof Looper)) return;
        pAmount.set((Integer) context.getValueForNodeKey(network, Looper.KEY_AMOUNT));
        pIndex.set((Integer) context.getValueForNodeKey(network, Looper.KEY_INDEX));
        pPosition.set((Float) context.getValueForNodeKey(network, Looper.KEY_POSITION));
    }

}
