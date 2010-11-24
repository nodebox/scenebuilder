package nodebox.builtins.math;

import nodebox.node.*;

@Description("Compare two numeric values.")
@Category("Math")
public class ConditionalLogic extends Node {
    public final int COMPARE_EQUAL  = 0;
    public final int COMPARE_NOT_EQUAL   = 1;
    public final int COMPARE_GREATER_THAN  = 2;
    public final int COMPARE_LOWER_THAN  = 3;
    public final int COMPARE_GREATER_THAN_OR_EQUAL = 4;
    public final int COMPARE_LOWER_THAN_OR_EQUAL  = 5;

    public FloatPort pValue1 = new FloatPort(this, "value1", Port.Direction.INPUT, 0f);
    public IntPort pCondition = new IntPort(this, "condition", Port.Direction.INPUT, COMPARE_EQUAL);
    public FloatPort pValue2 = new FloatPort(this, "value2", Port.Direction.INPUT, 0f);
    public BooleanPort pOutput = new BooleanPort(this, "output", Port.Direction.OUTPUT);

    public ConditionalLogic() {
        pCondition.addMenuItem(COMPARE_EQUAL, "is Equal");
        pCondition.addMenuItem(COMPARE_NOT_EQUAL, "is Not Equal");
        pCondition.addMenuItem(COMPARE_GREATER_THAN, "is Greater Than");
        pCondition.addMenuItem(COMPARE_LOWER_THAN, "is Lower Than");
        pCondition.addMenuItem(COMPARE_GREATER_THAN_OR_EQUAL, "is Greater Than or Equal");
        pCondition.addMenuItem(COMPARE_LOWER_THAN_OR_EQUAL, "is Lower Than or Equal");
    }

    @Override
    public void execute(Context context, float time) {
        float value1 = pValue1.get();
        float value2 = pValue2.get();
        
        switch (pCondition.get()) {
            case COMPARE_EQUAL:
                pOutput.set(value1 == value2);
                break;
            case COMPARE_NOT_EQUAL:
                pOutput.set(value1 != value2);
                break;
            case COMPARE_GREATER_THAN:
                pOutput.set(value1 > value2);
                break;
            case COMPARE_LOWER_THAN:
                pOutput.set(value1 < value2);
                break;
            case COMPARE_GREATER_THAN_OR_EQUAL:
                pOutput.set(value1 >= value2);
                break;
            case COMPARE_LOWER_THAN_OR_EQUAL:
                pOutput.set(value1 <= value2);
                break;
            default:
                break;
        }
    }
}
