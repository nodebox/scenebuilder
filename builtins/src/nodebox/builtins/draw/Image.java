package nodebox.builtins.draw;

import nodebox.node.*;
import processing.core.PApplet;
import processing.core.PImage;

@Description("Draw an image")
public class Image extends DrawingNode {

    public final ImagePort pImage = new ImagePort(this, "image", Port.Direction.INPUT);
    public final FloatPort pX = new FloatPort(this, "x", Port.Direction.INPUT, 0f);
    public final FloatPort pY = new FloatPort(this, "y", Port.Direction.INPUT, 0f);

    @Override
    public void draw(PApplet g, Context context, double time) {
        PImage image = pImage.get();
        if (image != null) {
            g.image(pImage.get(), pX.get(), pY.get());
        }
    }
}
