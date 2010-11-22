package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.Drawable;

@Description("Invert an image.")
@Drawable(true)
@Category("Image")
public class Invert extends PointFilter {

    @Override
    public int filter(int x, int y, int v) {
        int a = v & 0xff000000;
        return a | (~v & 0x00ffffff);
    }
}


    