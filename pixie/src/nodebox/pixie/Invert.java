package nodebox.pixie;

import nodebox.node.Description;

@Description("Inverts an image.")
public class Invert extends PointFilter {

    @Override
    public int filter(int x, int y, int v) {
        int a = v & 0xff000000;
        return a | (~v & 0x00ffffff);
    }
}


    