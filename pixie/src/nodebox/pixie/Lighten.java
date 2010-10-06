package nodebox.pixie;

import nodebox.node.*;
import processing.core.PImage;
import processing.core.PConstants;

@Description("Lighten an image.")
public class Lighten extends Node {

    public final ImagePort pImage = new ImagePort(this, "image", Port.Direction.INPUT);
    public final IntPort pValue = new IntPort(this, "value", Port.Direction.INPUT);
    public final ImagePort pOutput = new ImagePort(this, "output", Port.Direction.OUTPUT);


    @Override
    public void execute(Context context, float time) {
        PImage in = pImage.get();
        if (in == null) return;
        in.loadPixels();
        PImage out = context.getApplet().createImage(in.width, in.height, PConstants.RGB);
        out.loadPixels();
        int size = in.width * in.height;
        int value = pValue.get();
        int v, r, g, b;
        for (int i=0; i < size; i++) {
            v = in.pixels[i];
            r = v >> 16 & 0xFF;
            g = v >> 8 & 0xFF;
            b = v & 0xFF;
            r += value;
            g += value;
            b += value;
            r = r > 255 ? 255 : r < 0 ? 0 : r;
            g = g > 255 ? 255 : g < 0 ? 0 : g;
            b = b > 255 ? 255 : b < 0 ? 0 : b;
            v = r << 16 | g << 8 | b;
            out.pixels[i] = v;
        }
        out.updatePixels();
        pOutput.set(out);
    }
}
