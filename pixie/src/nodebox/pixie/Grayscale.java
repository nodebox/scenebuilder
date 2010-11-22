package nodebox.pixie;

import nodebox.node.*;

@Description("Gray out an image by averaging each pixel with white.")
@Drawable
@Category("Image")
public class Grayscale extends PointFilter {

    @Override
    public int filter(int x, int y, int v) {
		int a = v & 0xff000000;
		int r = (v >> 16) & 0xff;
		int g = (v >> 8) & 0xff;
		int b = v & 0xff;
		v = (r * 77 + g * 151 + b * 28) >> 8;
		return a | (v << 16) | (v << 8) | v;
    }

}