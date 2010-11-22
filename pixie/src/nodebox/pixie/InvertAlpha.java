package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.Drawable;

@Description("Invert the alpha channel of an image.")
@Drawable(true)
@Category("Image")
public class InvertAlpha extends PointFilter {

    @Override
    public int filter(int x, int y, int v) {
        return v ^ 0xff000000;
    }

}