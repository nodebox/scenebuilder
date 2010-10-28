package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;

@Description("Invert the alpha channel of an image.")
@Category("Image")
public class InvertAlpha extends PointFilter {

    @Override
    public int filter(int x, int y, int v) {
        return v ^ 0xff000000;
    }

}