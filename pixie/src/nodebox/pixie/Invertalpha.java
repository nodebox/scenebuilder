package nodebox.pixie;

import nodebox.node.Description;
import nodebox.node.IntPort;
import nodebox.node.Port;

@Description("Inverts the alpha channel of an image")
public class Invertalpha extends PointFilter {

    @Override
    public int filter(int x, int y, int v) {
    return v ^ 0xff000000;
    }

}