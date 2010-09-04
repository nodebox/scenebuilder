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

    public static void setStyle(PApplet p, Color fill, Color stroke, float strokeWeight) {
        setFill(p, fill);
        setStroke(p, stroke);
        p.strokeWeight(strokeWeight);
    }

    public static void setStyle(PApplet p, ColorPort fillPort, ColorPort strokePort, FloatPort strokeWeightPort) {
        setStyle(p, fillPort.get(), strokePort.get(), Math.max(strokeWeightPort.get(), 0f));
    }

    public static float random(float min, float max, int seed) {
        Random random = new Random(seed * 10000);
        return min + random.nextFloat() * (max-min);
    }

}
