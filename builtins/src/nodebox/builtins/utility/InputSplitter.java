package nodebox.builtins.utility;

import nodebox.node.*;

@Description("Forward a given input.")
@Category("Utility")
public class InputSplitter extends Node {
    public VariantPort pInput = new VariantPort(this, "input", Port.Direction.INPUT);
    public VariantPort pOutput = new VariantPort(this, "output", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        pOutput.set(pInput.get());
    }
}
