package nodebox.util;

import nodebox.node.ColorPort;
import nodebox.node.FloatPort;
import processing.core.PApplet;

import java.awt.*;
import java.util.Random;

public class ProcessingSupport {

    public static void setFill(PApplet p, Color c) {
        p.fill(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public static void setFill(PApplet p, ColorPort port) {
        setFill(p, port.get());
    }

    public static void setStroke(PApplet p, Color c) {
        p.stroke(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public static void setStroke(PApplet p, ColorPort port) {
        setStroke(p, port.get());
    }


    /**
     * Set the drawing style as a combination of fill, stroke and stroke weight.
     * <p/>
     * This method supports null values for all arguments, disabling the style.
     * E.g. if the fill is null, noFill() will be called.
     *
     * @param p            the processing applet
     * @param fill         the fill color
     * @param stroke       the stroke color
     * @param strokeWeight the stroke weight
     */
    public static void setStyle(PApplet p, Color fill, Color stroke, float strokeWeight) {
        if (fill != null) {
            setFill(p, fill);
        } else {
            p.noFill();
        }
        if (stroke != null) {
            setStroke(p, stroke);
            p.strokeWeight(Math.max(strokeWeight, 0f));
        } else {
            p.noStroke();
        }
    }

    /**
     * Set the drawing style based on a number of common ports.
     * <p/>
     * This method support null values for all of the arguments, disabling the style.
     * E.g. if the fillPort is null, noFill() will be called.
     *
     * @param p                the processing applet
     * @param fillPort         the port that contains the fill color
     * @param strokePort       the port that contains the stroke color
     * @param strokeWeightPort the port that contains the stroke weight
     */
    public static void setStyle(PApplet p, ColorPort fillPort, ColorPort strokePort, FloatPort strokeWeightPort) {
        Color fill = fillPort != null ? fillPort.get() : null;
        Color stroke = strokePort != null ? strokePort.get() : null;
        float strokeWeight = strokeWeightPort != null ? strokeWeightPort.get() : 1f;
        setStyle(p, fill, stroke, strokeWeight);
    }

    public static float random(float min, float max, int seed) {
        Random random = new Random(seed * 10000);
        return min + random.nextFloat() * (max - min);
    }

}
