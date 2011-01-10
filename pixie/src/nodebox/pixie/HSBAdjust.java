package nodebox.pixie;

import nodebox.node.*;

import java.awt.*;

@Description("Adjust the hue, saturation and brightness components of the image.")
@Drawable
@Category("Image")
public class HSBAdjust extends PointFilter {
    public final FloatPort pHue = new FloatPort(this, "hue", Port.Direction.INPUT);
    public final FloatPort pSaturation = new FloatPort(this, "saturation", Port.Direction.INPUT);
    public final FloatPort pBrightness = new FloatPort(this, "brightness", Port.Direction.INPUT);
    private float[] hsb = new float[3];

    @Override
    public int filter(int x, int y, int v) {
        int a = v & 0xff000000;
        int r = (v >> 16) & 0xff;
        int g = (v >> 8) & 0xff;
        int b = v & 0xff;
        Color.RGBtoHSB(r, g, b, hsb);
        hsb[0] += pHue.get();
        while (hsb[0] < 0)
            hsb[0] += Math.PI*2;
        hsb[1] += pSaturation.get();
        if (hsb[1] < 0)
            hsb[1] = 0;
        else if (hsb[1] > 1.0)
            hsb[1] = 1.0f;
        hsb[2] += pBrightness.get();
        if (hsb[2] < 0)
            hsb[2] = 0;
        else if (hsb[2] > 1.0)
            hsb[2] = 1.0f;
        int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        return a | (rgb & 0xffffff);
    }
}
