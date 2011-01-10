package nodebox.builtins.text;

import nodebox.node.*;

import java.util.Locale;

@Description("Change the casing of a string.")
@Category("Text")
public class Casing extends Node {

    public static enum Operation {
        UNCHANGED, UPPERCASE, LOWERCASE
    }

    public StringPort pInput = new StringPort(this, "inputString", Port.Direction.INPUT);
    public StringPort pOperation = new StringPort(this, "operation", Port.Direction.INPUT, "uppercase");
    public StringPort pOutput = new StringPort(this, "outputString", Port.Direction.OUTPUT);

    public Casing() {
        pInput.setDisplayName("string");
        pOutput.setDisplayName("string");

        for (Operation op : Operation.values()) {
            String name = op.name();
            pOperation.addMenuItem(name.toLowerCase(Locale.US),
                    name.substring(0, 1) + name.substring(1).toLowerCase());
        }
    }

    @Override
    public void execute(Context context, float time) {
        switch (Operation.valueOf(pOperation.get().toUpperCase(Locale.US))) {
            case UPPERCASE:
                pOutput.set(pInput.get().toUpperCase());
                break;
            case LOWERCASE:
                pOutput.set(pInput.get().toLowerCase());
                break;
            default:
                pOutput.set(pInput.get());
                break;
        }
    }
}
