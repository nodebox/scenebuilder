package nodebox.builtins.image;

import nodebox.node.*;
import processing.core.PGraphics;
import processing.core.PImage;

@Description("Load an image from disk.")
@Category("Image")
public class LoadImage extends Node {

    private String loadedFileName;
    private PImage loadedImage;

    public final StringPort pFileName = new StringPort(this, "fileName", Port.Direction.INPUT);
    public final ImagePort pImage = new ImagePort(this, "output", Port.Direction.OUTPUT);


    @Override
    public void execute(Context context, float time) {
        String fileName = pFileName.get();
        if (!fileName.equals(loadedFileName)) {
            loadedImage = context.getApplet().loadImage(fileName);
            loadedFileName = fileName;
        }
        pImage.set(loadedImage);
    }

    @Override
    public void draw(PGraphics g, Context context, float time) {
        g.image(pImage.get(), 0, 0);
    }

}
