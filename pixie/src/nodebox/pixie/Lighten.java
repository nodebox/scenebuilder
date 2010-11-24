package nodebox.pixie;

import nodebox.node.*;

@Description("Lighten an image.")
@Drawable
@Category("Image")
public class Lighten extends PointFilter {

    public final IntPort pAmount = new IntPort(this, "amount", Port.Direction.INPUT);

    @Override
    public int filter(int x, int y, int v) {
        int amount = pAmount.get();
        return toColorValue(red(v) + amount, green(v) + amount, blue(v) + amount, alpha(v));
    }

}
