package nodebox.graphics;

import processing.core.PGraphics;

public class ProcessingGraphics {

  /**
     * Set the fill color. If the color is null, call noFill().
     *
     * @param g the processing context
     * @param c the color
     */
    public static void setFill(PGraphics g, Color c) {
        if (c != null) {
            g.fill(c.getRed()*g.colorModeX, c.getGreen()*g.colorModeY, c.getBlue()*g.colorModeZ, c.getAlpha()*g.colorModeA);
        } else {
            g.noFill();
        }
    }
     /**
     * Set the stroke color. If the color is null, call noFill().
     *
     * @param g the processing context
     * @param c the color
     */
    public static void setStroke(PGraphics g, Color c) {
        if (c != null) {
            g.stroke(c.getRed()*g.colorModeX, c.getGreen()*g.colorModeY, c.getBlue()*g.colorModeZ, c.getAlpha()*g.colorModeA);
        } else {
            g.noStroke();
        }
    }


    /**
     * Set the drawing style as a combination of fill, stroke and stroke weight.
     * <p/>
     * This method supports null values for all arguments, disabling the style.
     * E.g. if the fill is null, noFill() will be called.
     *
     * @param g            the processing applet
     * @param fill         the fill color
     * @param stroke       the stroke color
     * @param strokeWeight the stroke weight
     */
    public static void setStyle(PGraphics g, Color fill, Color stroke, float strokeWeight) {
        if (fill != null) {
            setFill(g, fill);
        } else {
            g.noFill();
        }
        if (stroke != null) {
            setStroke(g, stroke);
            g.strokeWeight(Math.max(strokeWeight, 0f));
        } else {
            g.noStroke();
        }
    }


}
