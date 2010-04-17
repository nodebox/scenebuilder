package scenebuilder.library.util;

import scenebuilder.model.Context;
import scenebuilder.model.Macro;
import scenebuilder.model.Port;

public class Iterator extends Macro {

    public static final String PORT_AMOUNT = "amount";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INDEX = "index";
    public static final String KEY_POSITION = "position";

    public Iterator() {
        super();
        setDisplayName("Iterator");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Run the contents of this macro multiple times.");
        addInputPort(Port.Type.INTEGER, PORT_AMOUNT, 10);
    }

    @Override
    public boolean execute(Context context, double time) {
        int amount = asInteger(PORT_AMOUNT);
        for (int i = 0; i < amount; i++) {
            double position = i / (double) amount;
            Context childContext = new Context(context);
            childContext.setValueForNodeKey(this, KEY_AMOUNT, amount);
            childContext.setValueForNodeKey(this, KEY_INDEX, i);
            childContext.setValueForNodeKey(this, KEY_POSITION, position);
            boolean success = super.execute(childContext, time);
            if (!success) return false;
        }
        return true;        
    }
}
