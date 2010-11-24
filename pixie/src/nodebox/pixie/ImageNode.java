package nodebox.pixie;

import nodebox.node.Context;
import nodebox.node.ImagePort;
import nodebox.node.Node;
import nodebox.node.Port;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class ImageNode extends Node {

    public final ImagePort pOutput = new ImagePort(this, "outputImage", Port.Direction.OUTPUT);

    public ImageNode() {
        pOutput.setDisplayName("image");
    }

    @Override
    public void execute(Context context, float time) {
        PImage image = cook(context, time);
        pOutput.set(image);
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        g.image(pOutput.get(), 0, 0);
    }

    public abstract PImage cook(Context context, float time);

}
