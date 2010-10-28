package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.IntPort;
import nodebox.node.Port;

@Description("Reduce the number of colors in an image.")
@Category("Image")
public class Posterize extends PointFilter {

    public final IntPort pValue = new IntPort(this, "value", Port.Direction.INPUT, 5);
    private int numLevels;
    private int[] levels;
    private boolean initialized = false;


    public void setNumLevels() {
        this.numLevels = pValue.get();
        initialized = false;
    }

    public void createLevels() {
        setNumLevels();
        levels = new int[256];
        if (numLevels != 1)
            for (int i = 0; i < 256; i++)
                levels[i] = 255 * (numLevels * i / 256) / (numLevels - 1);
    }

    @Override
    public int filter(int x, int y, int v) {
        if (!initialized) {
            initialized = true;
            createLevels();
        }
        int a = v & 0xff000000;
        int r = (v >> 16) & 0xff;
        int g = (v >> 8) & 0xff;
        int b = v & 0xff;
        r = levels[r];
        g = levels[g];
        b = levels[b];
        return a | (r << 16) | (g << 8) | b;
    }

}


