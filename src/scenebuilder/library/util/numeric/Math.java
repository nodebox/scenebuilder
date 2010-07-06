package scenebuilder.library.util.numeric;

import scenebuilder.model.Context;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

public class Math extends Node {

    public static final String PORT_V1 = "v1";
    public static final String PORT_OPERATION = "operation";
    public static final String PORT_V2 = "v2";
    public static final String PORT_RESULT = "result";


    public Math() {
        setAttribute(Node.DISPLAY_NAME_ATTRIBUTE, "Math");
        setAttribute(Node.DESCRIPTION_ATTRIBUTE, "Perform a mathematical operations on two numbers.");
        addInputPort(Port.Type.NUMBER, PORT_V1, 0.0);
        addInputPort(Port.Type.STRING, PORT_OPERATION, "+");
        addInputPort(Port.Type.NUMBER, PORT_V2, 0.0);
        addOutputPort(Port.Type.NUMBER, PORT_RESULT);
    }

    @Override
    public boolean execute(Context context, double time) {
        String op = asString(PORT_OPERATION);
        if (op.trim().length() != 1) return false;
        char opChar = op.charAt(0);
        double v1 = asNumber(PORT_V1);
        double v2 = asNumber(PORT_V2);
        double result;
        switch (opChar) {
            case '+':
                result = v1 + v2;
                break;
            case '-':
                result = v1 - v2;
                break;
            case '*':
                result = v1 * v2;
                break;
            case '/':
                result = v1 / v2;
                break;
            case '%':
                result = v1 % v2;
                break;
            default:
                return false;
        }
        setValue(PORT_RESULT, result);
        return true;
    }
}
