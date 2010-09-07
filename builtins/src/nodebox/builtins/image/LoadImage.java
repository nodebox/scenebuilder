package nodebox.builtins.image;

import nodebox.node.*;
import processing.core.PImage;

@Description("Load an image from disk.")
public class LoadImage extends Node {

    private String loadedFileName;
    private PImage loadedImage;

    public final StringPort pFileName = new StringPort(this, "fileName", Port.Direction.INPUT);
    public final ImagePort pImage = new ImagePort(this, "image", Port.Direction.OUTPUT);


    @Override
    public void execute(Context context, double time) {
        String fileName = pFileName.get();
        if (!fileName.equals(loadedFileName)) {
            loadedImage = context.getApplet().loadImage(fileName);
            loadedFileName = fileName;
        }
        pImage.set(loadedImage);
    }
}
