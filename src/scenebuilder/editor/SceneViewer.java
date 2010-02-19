package scenebuilder.editor;

import scenebuilder.model.Connection;
import scenebuilder.model.Macro;
import scenebuilder.model.Node;
import scenebuilder.model.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SceneViewer extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    public static final String SELECT_PROPERTY = "SceneViewer.select";

    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);
    private static final Color GRID_COLOR = new Color(64, 64, 64);
    private static final int GRID_SPACING = 50;
    private static final Color CONNECTION_COLOR = new Color(200, 120, 70);
    private static final Stroke CONNECTION_STROKE = new BasicStroke(2);


    private final Scene scene;
    private Macro currentMacro;
    private double zoomFactor = 1.0;
    private double centerX = 0.0;
    private double centerY = 0.0;
    private Set<Node> selection = new HashSet<Node>();
    private boolean spaceDown;
    private int px, py;
    private HashMap<Node, NodeView> nodeViews = new HashMap<Node, NodeView>();

    public SceneViewer(Scene scene) {
        this.scene = scene;
        setCurrentMacro(scene.getRootMacro());
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setLayout(null);
        setFocusable(true);
    }

    public void setCurrentMacro(Macro currentMacro) {
        nodeViews.clear();
        this.currentMacro = currentMacro;
        for (Node node : currentMacro.getChildren()) {
            NodeView view = new NodeView(this, node);
            nodeViews.put(node, view);
            //add(view);
        }
    }

    //// Selections ////

    public Node getNode(NodeView view) {
        for (Map.Entry<Node, NodeView> entry : nodeViews.entrySet()) {
            if (entry.getValue() == view)
                return entry.getKey();
        }
        return null;
    }

    public NodeView getNodeView(Node node) {
        return nodeViews.get(node);
    }

    public boolean isSelected(Node node) {
        return selection.contains(node);
    }

    public boolean isSelected(NodeView view) {
        return isSelected(getNode(view));
    }

    public void singleSelect(Node node) {
        selection.clear();
        selection.add(node);
        repaint();
        firePropertyChange(SELECT_PROPERTY, null, selection);
    }

    public void singleSelect(NodeView nodeView) {
        singleSelect(getNode(nodeView));
    }

    public void deselectAll() {
        selection.clear();
        repaint();
        firePropertyChange(SELECT_PROPERTY, null, selection);
    }

    public void dragSelection(int dx, int dy) {
        for (Node node : selection) {
            NodeView view = getNodeView(node);
            view.setLocation(view.getX() + dx, view.getY() + dy);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle clip = g2.getClipBounds();
        Dimension size = getSize();
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);
        g2.setColor(GRID_COLOR);
        for (double y = centerY % GRID_SPACING; y < size.height; y += GRID_SPACING * zoomFactor) {
            g2.drawLine(0, (int) y, size.width , (int) y);
        }

        for (double x = centerX % GRID_SPACING; x < size.width ; x += GRID_SPACING * zoomFactor) {
            g2.drawLine((int) x, 0, (int) x, size.height);
        }

        g2.scale(zoomFactor, zoomFactor);
        g2.translate(centerX, centerY);

        paintConnections(g2);

        for (NodeView view : nodeViews.values()) {
            view.paintComponent(g);
        }
    }

    private void paintConnections(Graphics2D g) {
        for (Connection c : currentMacro.getConnections()) {
            NodeView outputView = nodeViews.get(c.getOutputNode());
            NodeView inputView = nodeViews.get(c.getInputNode());
            float outputX = outputView.getX() + outputView.getWidth() - 10;
            float outputY = outputView.getY() + outputView.getPortPosition(c.getOutputPort());
            float inputX = inputView.getX() + 10;
            float inputY = inputView.getY() + inputView.getPortPosition(c.getInputPort());
            float dx = Math.abs(inputX - outputX) / 2;
            GeneralPath p = new GeneralPath();
            p.moveTo(outputX, outputY);
            p.curveTo(inputX, outputY, inputX - dx, inputY, inputX, inputY);
            g.setColor(CONNECTION_COLOR);
            g.setStroke(CONNECTION_STROKE);
            g.draw(p);
        }
    }

    private NodeView getNodeAt(Point p) {
        Point transformedPoint = new Point(p);
        transformedPoint.translate(-(int)centerX, -(int)centerY);
        for (NodeView view : nodeViews.values()) {
            if (view.getBounds().contains(transformedPoint)) return view;
        }
        return null;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        px = e.getX();
        py = e.getY();
        NodeView view = getNodeAt(e.getPoint());
        if (view != null) {
            singleSelect(view);
        } else {
            deselectAll();
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - px;
        int dy = e.getY() - py;
        if (spaceDown) {
            centerX += dx;
            centerY += dy;
            repaint();
        } else {
            NodeView view = getNodeAt(e.getPoint());
            if (view != null) {
                dragSelection(dx, dy);
            } else {
                // Drag rectangle
            }
        }
        px = e.getX();
        py = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
    }


    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceDown = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceDown = false;
        }
    }
}
