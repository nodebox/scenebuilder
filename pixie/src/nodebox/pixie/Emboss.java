package nodebox.pixie;

import nodebox.node.Category;
import nodebox.node.Description;
import nodebox.node.Drawable;

@Description("A simple embossing filter.")
@Drawable
@Category("Image")
public class Emboss extends ConvolveFilter {

    public float[][] getKernel() {
        //float v = 1f / 9f;
        return new float[][] { { -1.0f, -1.0f, 0.0f },
                               { -1.0f, 1.0f, 1.0f },
                               { 0.0f, 1.0f, 1.0f } };
    }
}



/*
  	private static float[] embossMatrix = {
		-1.0f, -1.0f,  0.0f,
		-1.0f,  1.0f,  1.0f,
		 0.0f,  1.0f,  1.0f
	};

*/