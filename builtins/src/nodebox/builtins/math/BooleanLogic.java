package nodebox.builtins.math;

import nodebox.node.*;

@Description("Perform a boolean operation on two given boolean values.")
@Category("Math")
public class BooleanLogic extends Node {

    public final int LOGIC_AND  = 0;
    public final int LOGIC_OR   = 1;
    public final int LOGIC_XOR  = 2;
    public final int LOGIC_NOT  = 3;
    public final int LOGIC_NAND = 4;
    public final int LOGIC_NOR  = 5;
    public final int LOGIC_NXOR = 6;

    public BooleanPort pOperand1 = new BooleanPort(this, "first operand", Port.Direction.INPUT, false);
    public IntPort pOperation = new IntPort(this, "operation", Port.Direction.INPUT, LOGIC_AND);
    public BooleanPort pOperand2 = new BooleanPort(this, "second operand", Port.Direction.INPUT, false);
    public BooleanPort pOutput = new BooleanPort(this, "result", Port.Direction.OUTPUT);

    public BooleanLogic() {
        pOperation.addMenuItem(LOGIC_AND, "AND");
        pOperation.addMenuItem(LOGIC_OR, "OR");
        pOperation.addMenuItem(LOGIC_XOR, "XOR");
        pOperation.addMenuItem(LOGIC_NOT, "NOT");
        pOperation.addMenuItem(LOGIC_NAND, "NAND");
        pOperation.addMenuItem(LOGIC_NOR, "NOR");
        pOperation.addMenuItem(LOGIC_NXOR, "NXOR");
    }

    @Override
    public void execute(Context context, float time) {
        boolean value1 = pOperand1.get();
        boolean value2 = pOperand2.get();
        switch (pOperation.get()) {
            case LOGIC_AND:
                pOutput.set(value1 && value2);
                break;
            case LOGIC_OR:
                pOutput.set(value1 || value2);
                break;
            case LOGIC_XOR:
                pOutput.set(value1 ^ value2);
                break;
            case LOGIC_NOT:
                pOutput.set(! value1);
                break;
            case LOGIC_NAND:
                pOutput.set(!(value1 && value2));
                break;
            case LOGIC_NOR:
                pOutput.set(!(value1 || value2));
                break;
            case LOGIC_NXOR:
                pOutput.set(!(value1 ^ value2));
                break;
            default:
                break;
        }
    }
}
