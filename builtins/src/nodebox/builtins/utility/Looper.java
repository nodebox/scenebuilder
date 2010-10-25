package nodebox.builtins.utility;

import nodebox.node.*;

/**
 * The looper is a network that executes its context multiple times, according to the amount.
 * <p/>
 * The accompanying looper variables node can be used to get information on the current iteration,
 * such as the index, total amount, and position (between 0.0-1.0).
 */
@Description("Run the contents of this network multiple times.")
public class Looper extends Network {

    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.INPUT, 10);
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INDEX = "index";
    public static final String KEY_POSITION = "position";

    @Override
    public void execute(Context context, float time) {
        int amount = pAmount.get();
        float delta = amount > 2 ? 1f / (amount - 1f) : 1;
        float position;
        for (int i = 0; i < amount; i++) {
            position = i * delta;
            Context childContext = new Context(context);
            childContext.setValueForNodeKey(this, KEY_AMOUNT, amount);
            childContext.setValueForNodeKey(this, KEY_INDEX, i);
            childContext.setValueForNodeKey(this, KEY_POSITION, position);
            super.execute(childContext, time);
        }
    }
}
