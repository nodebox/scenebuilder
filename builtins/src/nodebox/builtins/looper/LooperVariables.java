package nodebox.builtins.looper;

import nodebox.node.*;

/**
 * The looper variables can be used in a looper network to get information on the current iteration.
 * <p/>
 * For each iteration, the looper variables provide the index, the total amount of executions,
 * and the position (between 0.0-1.0).
 */
public class LooperVariables extends Node {

    public final IntPort pAmount;
    public final IntPort pIndex;
    public final FloatPort pPosition;

    public LooperVariables() {
        setDisplayName("Looper Context");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Variables that contain information for every loop execution.");
        pAmount = (IntPort) addPort(new IntPort(this, "amount", Port.Direction.OUTPUT, 0));
        pIndex = (IntPort) addPort(new IntPort(this, "index", Port.Direction.OUTPUT, 0));
        pPosition = (FloatPort) addPort(new FloatPort(this, "position", Port.Direction.OUTPUT, 0f));
    }

    @Override
    public boolean execute(Context context, double time) {
        Network network = getNetwork();
        if (network == null) return true;
        if (!(network instanceof Looper)) return true;
        pAmount.set((Integer) context.getValueForNodeKey(network, Looper.KEY_AMOUNT));
        pIndex.set((Integer) context.getValueForNodeKey(network, Looper.KEY_INDEX));
        pPosition.set((Float) context.getValueForNodeKey(network, Looper.KEY_POSITION));
        return true;
    }

}
