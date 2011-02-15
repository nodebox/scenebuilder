package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.Drawable;

@Description("A simple sharpen filter.")
@Drawable
@Category("Image")
public class Sharpen extends ConvolveFilter {

    public float[][] getKernel() {
        //float v = 1f / 9f;
        return new float[][] { { 0.0f, -0.2f, 0.0f },
                               { -0.2f, 1.8f, -0.2f },
                               { 0.0f, -0.2f, 0.0f } };
    }
}
