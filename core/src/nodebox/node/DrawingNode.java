package nodebox.node;

import processing.core.PGraphics;

public abstract class DrawingNode extends Node {

    public final BooleanPort pEnabled = new BooleanPort(this, "enabled", Port.Direction.INPUT, true);

    @Override
    public boolean canDraw() {
        return true;
    }

    @Override
    public void execute(Context context, float time) {
        if (!pEnabled.get()) return;
        draw(context.getGraphics(), context, time);
    }

    public abstract void draw(PGraphics g, Context context, float time);

}
