package nodebox.pixie;

import nodebox.node.*;

@Description("Reduce the number of colors in an image.")
@Drawable(true)
@Category("Image")
public class Posterize extends PointFilter {

    public final IntPort pLevels = new IntPort(this, "levels", Port.Direction.INPUT, 5);
    private int levels = -1;
    private int[] levelArray;

    public void createLevels(int levels) {
        this.levels = levels;
        levelArray = new int[256];
        if (levels != 1)
            for (int i = 0; i < 256; i++)
                levelArray[i] = 255 * (levels * i / 256) / (levels - 1);
    }

    @Override
    public int filter(int x, int y, int v) {
        if (levels != pLevels.get()) {
            createLevels(pLevels.get());
        }
        int a = v & 0xff000000;
        int r = (v >> 16) & 0xff;
        int g = (v >> 8) & 0xff;
        int b = v & 0xff;
        r = levelArray[r];
        g = levelArray[g];
        b = levelArray[b];
        return a | (r << 16) | (g << 8) | b;
    }

}


