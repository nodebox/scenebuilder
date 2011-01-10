package nodebox.pixie;

import nodebox.node.Context;
import nodebox.node.ImagePort;
import nodebox.node.Port;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class ConvolveFilter extends ImageNode {
    public final ImagePort pImage = new ImagePort(this, "inputImage", Port.Direction.INPUT);

    public ConvolveFilter() {
        pImage.setDisplayName("image");
    }

    public abstract float[][] getKernel();

    @Override
    public PImage cook(Context context, float time) {
        if (pImage.get() == null) return null;
        PApplet applet = context.getApplet();
        PGraphics graphics = context.getGraphics();

        PImage source = pImage.get();
        PImage destination = applet.createImage(source.width, source.height, applet.RGB);

        int[] pixels = source.pixels;
        int[] dpixels = destination.pixels;
        float[][] kernel = getKernel();

        // Loop through every pixel in the image.
        for (int y = 1; y < source.height - 1; y++) { // Skip top and bottom edges
            for (int x = 1; x < source.width - 1; x++) { // Skip left and right edges
                float sumR = 0;
                float sumG = 0;
                float sumB = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        // Calculate the adjacent pixel for this kernel point
                        int pos = (y + ky) * source.width + (x + kx);

                        float valR = graphics.red(pixels[pos]);
                        float valG = graphics.green(pixels[pos]);
                        float valB = graphics.blue(pixels[pos]);
                        // Multiply adjacent pixels based on the kernel values
                        sumR += kernel[ky + 1][kx + 1] * valR;
                        sumG += kernel[ky + 1][kx + 1] * valG;
                        sumB += kernel[ky + 1][kx + 1] * valB;
                    }
                }
                // For this pixel in the new image, set the gray value
                // based on the sum from the kernel
                dpixels[y * source.width + x] = graphics.color(sumR, sumG, sumB);
            }
        }
        return destination;
    }
}
