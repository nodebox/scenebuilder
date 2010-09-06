package nodebox.node;

import processing.core.PApplet;

public abstract class DrawingNode extends Node {

    public final BooleanPort pEnabled = new BooleanPort(this, "enabled", Port.Direction.INPUT, true);

    @Override
    public boolean canDraw() {
        return true;
    }

    @Override
    public void execute(Context context, double time) {
        if (!pEnabled.get()) return;
        draw(context.getApplet(), context, time);
    }

    public abstract void draw(PApplet g, Context context, double time);

}
