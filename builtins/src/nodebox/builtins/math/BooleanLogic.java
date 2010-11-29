package nodebox.builtins.math;

import nodebox.node.*;

import java.util.Locale;

@Description("Perform a boolean operation on two given boolean values.")
@Category("Math")
public class BooleanLogic extends Node {

    public static enum Operation {
        AND, OR, XOR, NOT, NAND, NOR, NXOR
    }

    public BooleanPort pOperand1 = new BooleanPort(this, "operand1", Port.Direction.INPUT, false);
    public StringPort pOperation = new StringPort(this, "operation", Port.Direction.INPUT, "and");
    public BooleanPort pOperand2 = new BooleanPort(this, "operand2", Port.Direction.INPUT, false);
    public BooleanPort pOutput = new BooleanPort(this, "output", Port.Direction.OUTPUT);

    public BooleanLogic() {
        for (Operation op : Operation.values()) {
            pOperation.addMenuItem(op.name().toLowerCase(Locale.US), op.name());
        }
    }

    @Override
    public void execute(Context context, float time) {
        try {
            boolean value1 = pOperand1.get();
            boolean value2 = pOperand2.get();

            switch (Operation.valueOf(pOperation.get().toUpperCase(Locale.US))) {
                case AND:
                    pOutput.set(value1 && value2);
                    break;
                case OR:
                    pOutput.set(value1 || value2);
                    break;
                case XOR:
                    pOutput.set(value1 ^ value2);
                    break;
                case NOT:
                    pOutput.set(! value1);
                    break;
                case NAND:
                    pOutput.set(!(value1 && value2));
                    break;
                case NOR:
                    pOutput.set(!(value1 || value2));
                    break;
                case NXOR:
                    pOutput.set(!(value1 ^ value2));
                    break;
                default:
                    break;
            }

        } catch (IllegalArgumentException e) {
            pOutput.set(false);
            return;
        }
    }
}
