package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.IntPort;
import nodebox.node.Port;

@Description("Saturate an image.")
@Category("Image")
public class Saturation extends PointFilter {

    public final IntPort pValue = new IntPort(this, "value", Port.Direction.INPUT,5);

    public static int clamp(int c) {
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}

    @Override
    public int filter(int x, int y, int v) {
        int amount = pValue.get();

        	if ( amount != 1 ) {
            int a = v & 0xff000000;
            int r = (v >> 16) & 0xff;
            int g = (v >> 8) & 0xff;
            int b = v & 0xff;
            int vv = ( r + g + b )/3;
            r = clamp( (int)(vv + amount * (r-vv)) );
            g = clamp( (int)(vv + amount * (g-vv)) );
            b = clamp( (int)(vv + amount * (b-vv)) );
            return a | (r << 16) | (g << 8) | b;
        }
        return v;
    }
}
