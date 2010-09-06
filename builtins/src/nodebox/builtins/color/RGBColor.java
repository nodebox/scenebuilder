package nodebox.builtins.color;

import nodebox.node.*;

import java.awt.*;

import static nodebox.util.ProcessingSupport.clamp;

@Description("Create a color using RGB values.")
public class RGBColor extends Node {

    public final FloatPort pRed = new FloatPort(this, "red", Port.Direction.INPUT);
    public final FloatPort pGreen = new FloatPort(this, "green", Port.Direction.INPUT);
    public final FloatPort pBlue = new FloatPort(this, "blue", Port.Direction.INPUT);
    public final FloatPort pAlpha = new FloatPort(this, "alpha", Port.Direction.INPUT, 255f);
    public final FloatPort pRange = new FloatPort(this, "range", Port.Direction.INPUT, 255f);
    public final ColorPort pColor = new ColorPort(this, "color", Port.Direction.OUTPUT, Color.BLACK);

    @Override
    public void execute(Context context, double time) {
        float range = Math.max(pRange.get(), 1);
        float red = clamp(pRed.get() / range);
        float green = clamp(pGreen.get() / range);
        float blue = clamp(pBlue.get() / range);
        float alpha = clamp(pAlpha.get() / range);
        pColor.set(new Color(red, green, blue, alpha));
    }
}
