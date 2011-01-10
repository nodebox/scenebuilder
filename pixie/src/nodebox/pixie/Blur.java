package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.Drawable;

@Description("Blur an image.")
@Drawable
@Category("Image")
public class Blur extends ConvolveFilter {

    public float[][] getKernel() {
        float v = 1f / 9f;
        return new float[][] { { v, v, v },
                               { v, v, v },
                               { v, v, v } };
    }
}
