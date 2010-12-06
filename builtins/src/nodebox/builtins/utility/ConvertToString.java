package nodebox.builtins.utility;

import nodebox.node.*;

@Description("Present given input as a string.")
@Category("Utility")
public class ConvertToString extends Node {
    public VariantPort pInput = new VariantPort(this, "input", Port.Direction.INPUT);
    public StringPort pOutput = new StringPort(this, "string", Port.Direction.OUTPUT);

    @Override
    public void execute(Context context, float time) {
        Connection conn = pInput.getConnection();
        if (conn != null && conn.getOutputPort() instanceof PersistablePort) {
            pOutput.set(((PersistablePort) conn.getOutputPort()).getValueAsString());
        } else if (pInput.get() != null) {
            pOutput.set(pInput.get().toString());
        } else {
            pOutput.set("");
        }
    }
}
