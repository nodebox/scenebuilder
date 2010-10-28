package nodebox.pixie;

import nodebox.node.*;

@Description("Adjust the red green and blue component of the image.")
@Category("Image")
public class RGBAdjust extends PointFilter {

    public final FloatPort pRed = new FloatPort(this, "red", Port.Direction.INPUT,(float).5);
    public final FloatPort pGreen = new FloatPort(this, "green", Port.Direction.INPUT,(float).5);
    public final FloatPort pBlue = new FloatPort(this, "blue", Port.Direction.INPUT,(float).5);


	public int[] getLUT() {
		int[] lut = new int[256];
		for ( int i = 0; i < 256; i++ ) {
			lut[i] = filter( 0, 0, (i << 24) | (i << 16) | (i << 8) | i );
		}
		return lut;
	}

    public static int clamp(int c) {
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}

    @Override
    public int filter(int x, int y, int v) {
		int a = v & 0xff000000;
		int r = (v >> 16) & 0xff;
		int g = (v >> 8) & 0xff;
		int b = v & 0xff;
		r = clamp((int)(r * pRed.get()));
		g = clamp((int)(g * pGreen.get()));
		b = clamp((int)(b * pBlue.get()));


		return a | (r << 16) | (g << 8) | b;
    }


}