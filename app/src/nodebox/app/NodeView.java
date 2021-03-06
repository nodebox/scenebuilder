package nodebox.app;

import nodebox.node.Network;
import nodebox.node.Node;
import nodebox.node.Port;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;

public class NodeView {

    public static final int PORT_WIDTH = 20;
    public static final int PORT_HEIGHT = 20;
    private static final Color NORMAL_PORT_COLOR = new Color(50, 50, 52);
    private static final Color NORMAL_PORT_TEXT_COLOR = new Color(115, 115, 115);
    private static final Color START_PORT_COLOR = new Color(107, 95, 172);
    private static final Color END_PORT_COLOR = new Color(166, 162, 95);
    private static final int NODE_HEADER_HEIGHT = 20;
    private static final int NODE_MINIMUM_WIDTH = 150;
    private static final Color NODE_NAME_COLOR = new Color(233, 233, 233);
    private static final Font NODE_NAME_FONT = Theme.SMALL_BOLD_FONT;
    private static final Color NODE_HEADER_COLOR = new Color(83, 83, 85, 200);
    private static final Color NODE_BODY_COLOR = new Color(39, 39, 41, 200);
    private static final Color NODE_PORT_COLOR = new Color(233, 233, 233);
    private static final Color NODE_PUBLISHED_PORT_COLOR = new Color(250, 250, 100);
    private static final Stroke NODE_PORT_STROKE = new BasicStroke(1);
    private static final Font NODE_PORT_FONT = Theme.INFO_FONT;
    private static final Color NODE_BORDER_COLOR = new Color(18, 18, 18);
    private static final Stroke NODE_BORDER_STROKE = new BasicStroke(2);
    private static final Color NODE_SELECTION_BORDER_COLOR = new Color(200, 18, 18);
    private static final Stroke NODE_SELECTION_BORDER_STROKE = new BasicStroke(3);
    private static final Color NODE_RENDERED_COLOR = new Color(202, 197, 59);
    private static final int VERTICAL_TEXT_NUDGE = 6;

    private final NetworkViewer networkViewer;
    private final Node node;
    private Shape nodeRect;
    private int totalWidth;
    private int totalHeight;
    private int px, py;
    private Rectangle bounds;

    public NodeView(NetworkViewer networkViewer, Node node) {
        this.networkViewer = networkViewer;
        this.node = node;
        calculateBounds();
    }
    //// Bounds ////


    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void setLocation(int x, int y) {
        this.bounds = new Rectangle(x, y, bounds.width, bounds.height);
    }

    public int getX() {
        return bounds.x;
    }

    public int getY() {
        return bounds.y;
    }

    public int getWidth() {
        return bounds.width;
    }

    public int getHeight() {
        return bounds.height;
    }

    private void calculateBounds() {
        Point pos = node.getPosition();
        java.util.List<Port> inputPorts = node.getInputPorts();
        java.util.List<Port> outputPorts = node.getOutputPorts();
        int maxAmount = Math.max(Math.max(inputPorts.size(), outputPorts.size()), 1);
        totalHeight = NODE_HEADER_HEIGHT + maxAmount * PORT_HEIGHT;
        totalWidth = NODE_MINIMUM_WIDTH;
        if (node instanceof Network) {
            nodeRect = new Rectangle2D.Float(0, 0, totalWidth, totalHeight);
        } else {
            nodeRect = new RoundRectangle2D.Float(0, 0, totalWidth, totalHeight, 7, 7);
        }
        setBounds(pos.x, pos.y, totalWidth, totalHeight);
    }

    public int getPortIndex(Port p) {
        java.util.List<Port> ports;
        if (p.getDirection() == Port.Direction.INPUT) {
            ports = node.getInputPorts();
        } else {
            ports = node.getOutputPorts();
        }
        return ports.indexOf(p);
    }

    public int getPortPosition(Port p) {
        return NODE_HEADER_HEIGHT + PORT_HEIGHT * getPortIndex(p);
    }

    public Rectangle getPortBounds(Port p) {
        int y = getPortPosition(p) + bounds.y;
        int x = bounds.x;
        if (p.getDirection() == Port.Direction.OUTPUT) {
            x = bounds.x + totalWidth - PORT_WIDTH;
        }
        return new Rectangle(x, y, PORT_WIDTH, PORT_HEIGHT);
    }

    public Port getPortAt(Point p) {
        for (Port port : node.getPorts()) {
            Rectangle portBounds = getPortBounds(port);
            if (portBounds.contains(p)) {
                return port;
            }
        }
        return null;
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();
        g2.translate(bounds.x, bounds.y);
        Shape originalClip = g2.getClip();
        g2.setColor(NODE_BODY_COLOR);

        // Draw the node
        g2.fill(nodeRect);

        // Clip to the node contents
        g2.clip(nodeRect);

        // Draw the header
        g2.setColor(NODE_HEADER_COLOR);
        g2.fillRect(0, 0, totalWidth, NODE_HEADER_HEIGHT);
        g2.setFont(NODE_NAME_FONT);
        g2.setColor(NODE_NAME_COLOR);
        g2.drawString(node.getDisplayName(), 10, NODE_NAME_FONT.getSize() + VERTICAL_TEXT_NUDGE);

        // Prepare state for drawing the ports
        g2.setFont(NODE_PORT_FONT);

        // Draw input ports
        int y = NODE_HEADER_HEIGHT;
        Collection<Port> inputPorts = node.getInputPorts();
        for (Port port : inputPorts) {
            g2.setColor(getPortColor(port));
            g2.fillRect(0, y, PORT_WIDTH, PORT_HEIGHT);
            g2.setColor(NORMAL_PORT_TEXT_COLOR);
            g2.drawString(port.getDisplayName(), PORT_WIDTH + 5, y + VERTICAL_TEXT_NUDGE + NODE_PORT_FONT.getSize());
            y += PORT_HEIGHT;
        }

        // Draw output ports
        // Reset y to draw the output ports.
        y = NODE_HEADER_HEIGHT;
        Collection<Port> outputPorts = node.getOutputPorts();
        for (Port port : outputPorts) {
            g2.setColor(getPortColor(port));
            g2.fillRect(totalWidth - PORT_WIDTH, y, PORT_WIDTH, PORT_HEIGHT);
            g2.setColor(NORMAL_PORT_TEXT_COLOR);
            Rectangle2D r = g2.getFontMetrics().getStringBounds(port.getDisplayName(), g);
            g2.drawString(port.getDisplayName(), totalWidth - (int) r.getWidth() - PORT_WIDTH - 5, y + VERTICAL_TEXT_NUDGE + NODE_PORT_FONT.getSize());
            y += PORT_HEIGHT;
        }

        // Draw the border
        g2.setClip(originalClip);
        if (networkViewer.isSelected(this)) {
            g2.setColor(NODE_SELECTION_BORDER_COLOR);
            g2.setStroke(NODE_SELECTION_BORDER_STROKE);
        } else {
            g2.setColor(NODE_BORDER_COLOR);
            g2.setStroke(NODE_BORDER_STROKE);
        }
        g2.draw(nodeRect);

        // If the node is rendered, draw the yellow line on top.
        if (node.isRenderedNode()) {
            g2.clip(nodeRect);
            g2.setColor(NODE_RENDERED_COLOR);
            g2.fillRect(0, 0, totalWidth, 4);
            g2.setClip(originalClip);
        }

        // Restore original state.
        g2.setTransform(originalTransform);
    }

    /**
     * Get the port color depending on if it's the starting port, end port or a normal port.
     */
    private Color getPortColor(Port p) {
        if (networkViewer.isConnectionStartPort(p)) {
            return START_PORT_COLOR;
        } else if (networkViewer.isConnectionEndPort(p)) {
            return END_PORT_COLOR;
        } else {
            return NORMAL_PORT_COLOR;
        }
    }

}
