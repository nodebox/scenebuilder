package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.Drawable;

@Description("Find edges of an image.")
@Drawable
@Category("Image")
public class EdgeDetect extends ConvolveFilter {

    public float[][] getKernel() {
        return new float[][] { { -1, -1, -1 },
                               { -1,  9, -1 },
                               { -1, -1, -1 } };
    }
}
