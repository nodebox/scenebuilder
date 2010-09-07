package nodebox.graphics.nodes;

import nodebox.graphics.Colorizable;
import nodebox.node.ColorPort;
import nodebox.node.FloatPort;

import java.awt.*;

public class GraphicsSupport {

    public static nodebox.graphics.Color toNodeBoxColor(Color c) {
        return new nodebox.graphics.Color(c);
    }

    public static void setStyle(Colorizable c, Color fillColor, Color strokeColor, float strokeWeight) {
        if (fillColor != null) {
            c.setFillColor(toNodeBoxColor(fillColor));
        } else {
            c.setFillColor(null);
        }
        if (strokeColor != null && strokeWeight > 0f) {
            c.setStrokeColor(toNodeBoxColor(strokeColor));
            c.setStrokeWidth(strokeWeight);
        } else {
            c.setStrokeColor(null);
        }
    }

    public static void setStyle(Colorizable c, ColorPort fillPort, ColorPort strokePort, FloatPort strokeWeightPort) {
        Color fillColor = fillPort != null ? fillPort.get() : null;
        Color strokeColor = strokePort != null ? strokePort.get() : null;
        float strokeWeight = strokeWeightPort != null ? strokeWeightPort.get() : 1f;
        setStyle(c, fillColor, strokeColor, strokeWeight);
    }

}
