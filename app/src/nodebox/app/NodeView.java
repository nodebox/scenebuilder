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
    private static final Color NORMAL_PORT_COLOR = new Color(115, 115, 115);
    private static final Color START_PORT_COLOR = new Color(107, 95, 172);
    private static final Color END_PORT_COLOR = new Color(166, 162, 95);
    private static final int NODE_HEADER_HEIGHT = 20;
    private static final int NODE_MINIMUM_WIDTH = 150;
    private static final Color NODE_NAME_COLOR = new Color(233, 233, 233);
    private static final Font NODE_NAME_FONT = new Font(Font.DIALOG, Font.BOLD, 10);
    private static final Color NODE_HEADER_COLOR = new Color(83, 83, 85, 200);
    private static final Color NODE_BODY_COLOR = new Color(39, 39, 41, 200);
    private static final Color NODE_PORT_COLOR = new Color(233, 233, 233);
    private static final Color NODE_PUBLISHED_PORT_COLOR = new Color(250, 250, 100);
    private static final Stroke NODE_PORT_STROKE = new BasicStroke(1);
    private static final Font NODE_PORT_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);
    private static final Color NODE_BORDER_COLOR = new Color(18, 18, 18);
    private static final Stroke NODE_BORDER_STROKE = new BasicStroke(2);
    private static final Color NODE_SELECTION_BORDER_COLOR = new Color(200, 18, 18);
    private static final Stroke NODE_SELECTION_BORDER_STROKE = new BasicStroke(3);
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
            nodeRect = new Rectangle2D.Float(3, 3, totalWidth, totalHeight);
        } else {
            nodeRect = new RoundRectangle2D.Float(3, 3, totalWidth, totalHeight, 7, 7);
        }
        setBounds(pos.x - 3, pos.y - 3, totalWidth + 6, totalHeight + 6);
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
            x = totalWidth - PORT_WIDTH;
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
        g2.translate(3, 3);
        g2.setColor(NODE_BODY_COLOR);

        g2.fill(nodeRect);
        g2.clip(nodeRect);
        g2.setColor(NODE_HEADER_COLOR);
        g2.fillRect(0, 0, totalWidth + 3, NODE_HEADER_HEIGHT); // TODO: Find out why the +3 needs to be here.
        g2.setFont(NODE_NAME_FONT);
        g2.setColor(NODE_NAME_COLOR);
        g2.drawString(node.getDisplayName(), 10, NODE_NAME_FONT.getSize() + VERTICAL_TEXT_NUDGE);
        g2.setClip(originalClip);
        g2.setColor(NODE_PORT_COLOR);
        int y = NODE_HEADER_HEIGHT;
        g2.setFont(NODE_PORT_FONT);
        g2.setStroke(NODE_PORT_STROKE);
        Network network = node.getNetwork();
        Collection<Port> inputPorts = node.getInputPorts();
        for (Port port : inputPorts) {
            g2.setColor(getPortColor(port));
            g2.fillRect(3, y, PORT_WIDTH, PORT_HEIGHT);
            if (network != null && network.isPublished(port)) {
                g2.setColor(NODE_PUBLISHED_PORT_COLOR);
                g2.fillOval(9, y - 6, 4, 4);
                g2.setColor(NODE_PORT_COLOR);
            }
            g2.drawOval(9, y - 6, 4, 4);
            g2.drawString(port.getDisplayName(), 20, y + VERTICAL_TEXT_NUDGE + NODE_PORT_FONT.getSize());
            y += PORT_HEIGHT;
        }
        // Reset y to draw the output ports.
        y = NODE_HEADER_HEIGHT;
        Collection<Port> outputPorts = node.getOutputPorts();
        for (Port port : outputPorts) {
            g2.setColor(getPortColor(port));
            g2.fillRect(totalWidth - 3 - PORT_WIDTH, y, PORT_WIDTH, PORT_HEIGHT);
            if (network != null && network.isPublished(port)) {
                g2.setColor(NODE_PUBLISHED_PORT_COLOR);
                g2.fillOval(9, y - 6, 4, 4);
                g2.setColor(NODE_PORT_COLOR);
            }
            Rectangle2D r = g2.getFontMetrics().getStringBounds(port.getDisplayName(), g);
            g2.drawString(port.getDisplayName(), totalWidth - (int) r.getWidth() - 20, y + VERTICAL_TEXT_NUDGE + NODE_PORT_FONT.getSize());
            g2.drawOval(totalWidth - 13, y - 6, 4, 4);
            y += PORT_HEIGHT;
        }

        if (networkViewer.isSelected(this)) {
            g2.setColor(NODE_SELECTION_BORDER_COLOR);
            g2.setStroke(NODE_SELECTION_BORDER_STROKE);
        } else {
            g2.setColor(NODE_BORDER_COLOR);
            g2.setStroke(NODE_BORDER_STROKE);
        }
        g2.draw(nodeRect);
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
