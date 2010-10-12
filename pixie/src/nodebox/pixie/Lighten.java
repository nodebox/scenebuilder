package nodebox.pixie;

import nodebox.node.Description;
import nodebox.node.IntPort;
import nodebox.node.Port;

@Description("Lighten an image.")
public class Lighten extends PointFilter {

    public final IntPort pValue = new IntPort(this, "value", Port.Direction.INPUT);

    @Override
    public int filter(int x, int y, int v) {
        int value = pValue.get();
        return toColorValue(red(v) + value, green(v) + value, blue(v) + value, alpha(v));
    }

}
