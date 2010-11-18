package nodebox.app;

import nodebox.node.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkViewer extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    public static final String SELECT_PROPERTY = "NetworkViewer.select";

    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);
    private static final Color GRID_COLOR = new Color(64, 64, 64);
    private static final int GRID_SPACING = 50;
    private static final Color CONNECTION_COLOR = new Color(200, 120, 70);
    private static final Stroke CONNECTION_STROKE = new BasicStroke(2);
    private static BufferedImage backgroundImage;
    private static TexturePaint backgroundPaint;

    static {
        backgroundImage = PlatformUtils.loadImageResource("network-background.png");
        backgroundPaint = new TexturePaint(backgroundImage, new Rectangle(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null)));
    }

    private final Scene scene;
    private double zoomFactor = 1.0;
    private double centerX = 0.0;
    private double centerY = 0.0;
    private Set<Node> selection = new HashSet<Node>();
    private boolean spaceDown;
    private int px, py;
    private HashMap<Node, NodeView> nodeViews = new HashMap<Node, NodeView>();
    private JPopupMenu viewerPopup;
    private SceneDocument document;
    private JPopupMenu nodeViewPopup;
    private boolean isConnecting = false;
    private Port connectionStartPort;
    private Port connectionEndPort;

    public NetworkViewer(SceneDocument document, Scene scene) {
        this.document = document;
        this.scene = scene;
        setCurrentNetwork(scene.getRootNetwork());
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setLayout(null);
        setFocusable(true);
        initPopupMenus();
    }

    public Network getCurrentNetwork() {
        return document.getCurrentNetwork();
    }

    public void setCurrentNetwork(Network network) {
        updateView();
    }

    public void updateView() {
        resetView();
        nodeViews.clear();
        for (Node node : getCurrentNetwork().getChildren()) {
            NodeView view = new NodeView(this, node);
            nodeViews.put(node, view);
        }
        repaint();
    }

    private void initPopupMenus() {
        viewerPopup = new JPopupMenu();
        //viewerPopup.add(new NewNodeAction());
        viewerPopup.add(new ResetViewAction());
        //viewerPopup.add(new GoUpAction());
        nodeViewPopup = new JPopupMenu();
        addMouseListener(new PopupHandler());

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
            int x = view.getX() + dx;
            int y = view.getY() + dy;
            view.setLocation(x, y);
            node.setPosition(x, y);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle clip = g2.getClipBounds();
        g2.setPaint(backgroundPaint);
        g2.fill(clip);

        g2.scale(zoomFactor, zoomFactor);
        g2.translate(centerX, centerY);

        paintConnections(g2);
        if (isConnecting() && connectionStartPort != null) {
            Port port = connectionStartPort;
            NodeView nv = getNodeView(port.getNode());
            Rectangle portBounds = nv.getPortBounds(connectionStartPort);
            g2.drawLine((int) portBounds.getCenterX(), (int) portBounds.getCenterY(), px, py);
        }

        for (NodeView view : nodeViews.values()) {
            view.paintComponent(g);
        }
    }

    private void paintConnections(Graphics2D g) {
        for (Connection c : getCurrentNetwork().getConnections()) {
            NodeView outputView = nodeViews.get(c.getOutputNode());
            NodeView inputView = nodeViews.get(c.getInputNode());
            float outputX = outputView.getX() + outputView.getWidth() - 10;
            float outputY = outputView.getY() + outputView.getPortPosition(c.getOutputPort()) + NodeView.PORT_HEIGHT / 2;
            float inputX = inputView.getX() + 13;
            float inputY = inputView.getY() + inputView.getPortPosition(c.getInputPort()) + NodeView.PORT_HEIGHT / 2;
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
        transformedPoint.translate(-(int) centerX, -(int) centerY);
        for (NodeView view : nodeViews.values()) {
            if (view.getBounds().contains(transformedPoint)) return view;
        }
        return null;
    }

    private Port getPortAt(Point p) {
        NodeView view = getNodeAt(p);
        if (view == null) return null;
        return view.getPortAt(p);
    }

    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
        NodeView view = getNodeAt(e.getPoint());
        if (view == null) return;
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            new SetRenderedNodeAction(getNode(view)).actionPerformed(null);
        }
    }

    public void mousePressed(MouseEvent e) {
        px = e.getX();
        py = e.getY();
        NodeView view = getNodeAt(e.getPoint());
        if (view != null) {
            singleSelect(view);
            Port port = view.getPortAt(e.getPoint());
            if (port != null) {
                startConnection(port);
            }
        } else {
            deselectAll();
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        endConnection();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - px;
        int dy = e.getY() - py;
        px = e.getX();
        py = e.getY();
        if (spaceDown) {
            centerX += dx;
            centerY += dy;
            repaint();
        } else if (isConnecting()) {
            connectionEndPort = null;
            Port p = getPortAt(e.getPoint());
            if (p != null && connectionStartPort.canConnectTo(p)) {
                connectionEndPort = p;
            }
            repaint();
        } else {
            dragSelection(dx, dy);
        }
    }

    public void mouseMoved(MouseEvent e) {
        Port oldConnectionStartPort = connectionStartPort;
        connectionStartPort = getPortAt(e.getPoint());
        if (oldConnectionStartPort != connectionStartPort) {
            repaint();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            for (Node node : selection) {
                if (node.getNetwork() != null) {
                    node.getNetwork().removeChild(node);
                }
            }
            updateView();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceDown = false;
        }
    }

    public boolean checkForNodeViewPopup(MouseEvent e) {
        if (!e.isPopupTrigger()) return false;
        NodeView view = getNodeAt(e.getPoint());
        if (view == null) return false;
        Node node = getNode(view);
        nodeViewPopup.removeAll();

        JMenu publishInputsMenu = new JMenu("Publish Inputs");
        publishInputsMenu.setEnabled(false);
        for (Port p : node.getInputPorts()) {
            publishInputsMenu.setEnabled(true);
            publishInputsMenu.add(new PublishPortAction(p));
        }
        nodeViewPopup.add(publishInputsMenu);
        JMenu publishOutputsMenu = new JMenu("Publish Outputs");
        publishOutputsMenu.setEnabled(false);
        for (Port p : node.getOutputPorts()) {
            publishOutputsMenu.setEnabled(true);
            publishOutputsMenu.add(new PublishPortAction(p));
        }
        nodeViewPopup.add(publishOutputsMenu);

        if (node instanceof Network)
            nodeViewPopup.add(new EditNetworkAction((Network) node));
        nodeViewPopup.add(new RenameAction(node));
        nodeViewPopup.add(new SetRenderedNodeAction(node));
        nodeViewPopup.add(new DeleteAction(node));
        nodeViewPopup.show(NetworkViewer.this, e.getX(), e.getY());
        return true;
    }

    //// Connections ////


    /**
     * This method gets called when we start dragging a connection line from a port on a node view.
     *
     * @param port the port where we start from.
     */
    public void startConnection(Port port) {
        connectionStartPort = port;
        isConnecting = true;
    }

    /**
     * This method gets called when a dragging operation ends.
     * <p/>
     * We don't care if a connection was established or not.
     */
    public void endConnection() {
        if (connectionStartPort != null && connectionEndPort == null && connectionStartPort.isInputPort()) {
            // If we're dragging from an input port to nowhere, disconnect the port.
            getCurrentNetwork().disconnect(connectionStartPort);
        } else if (connectionStartPort != null && connectionEndPort != null && connectionStartPort.canConnectTo(connectionEndPort)) {
            Port input = connectionStartPort.getDirection() == Port.Direction.INPUT ? connectionStartPort : connectionEndPort;
            Port output = connectionStartPort.getDirection() == Port.Direction.OUTPUT ? connectionStartPort : connectionEndPort;
            getCurrentNetwork().connect(output, input);
        }
        connectionStartPort = null;
        connectionEndPort = null;
        isConnecting = false;
        repaint();
    }

    /**
     * Return true if we are in the middle of a connection drag operation.
     *
     * @return true if we are connecting nodes together.
     */
    public boolean isConnecting() {
        return isConnecting;
    }

    public boolean isConnectionStartPort(Port port) {
        return connectionStartPort == port;
    }

    public boolean isConnectionEndPort(Port port) {
        return connectionEndPort == port;
    }

    private class PopupHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            checkForPopUp(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkForPopUp(e);
        }

        public void checkForPopUp(MouseEvent e) {
            if (checkForNodeViewPopup(e)) return;
            if (e.isPopupTrigger()) {
                viewerPopup.removeAll();
                if (getCurrentNetwork().getNetwork() != null)
                    viewerPopup.add(new EditParentNetworkAction(getCurrentNetwork().getNetwork()));
                viewerPopup.add(new ResetViewAction());
                viewerPopup.show(NetworkViewer.this, e.getX(), e.getY());
            }
        }
    }

    private void resetView() {
        centerX = centerY = 0;
        zoomFactor = 1.0;
        repaint();
    }

    private class ResetViewAction extends AbstractAction {
        private ResetViewAction() {
            super("Reset View");
        }

        public void actionPerformed(ActionEvent e) {
            resetView();
        }
    }

    private class EditParentNetworkAction extends AbstractAction {
        private Network parent;

        private EditParentNetworkAction(Network parent) {
            super("Edit Parent Network");
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            document.setCurrentNetwork(parent);
        }

    }

    private class EditNetworkAction extends AbstractAction {
        private Network network;

        private EditNetworkAction(Network network) {
            super("Edit Child Network");
            this.network = network;
        }

        public void actionPerformed(ActionEvent e) {
            document.setCurrentNetwork(network);
        }
    }

    private class PublishPortAction extends AbstractAction {
        private Port port;

        private PublishPortAction(Port port) {
            super(port.getName());
            this.port = port;
        }

        public void actionPerformed(ActionEvent e) {
            Network network = port.getNode().getNetwork();
            if (network == null) return;
            if (network.isPublished(port)) {
                network.unPublishPort(port);
            } else {
                network.publishPort(port);
            }
            repaint();
        }
    }

    private class RenameAction extends AbstractAction {
        private Node node;

        private RenameAction(Node node) {
            super("Rename");
            this.node = node;
        }

        public void actionPerformed(ActionEvent e) {
            String s = JOptionPane.showInputDialog(NetworkViewer.this, "New name:", node.getDisplayName());
            if (s == null || s.length() == 0)
                return;
            node.setDisplayName(s);
            repaint();
        }
    }

    private class DeleteAction extends AbstractAction {
        private Node node;

        private DeleteAction(Node node) {
            super("Delete");
            this.node = node;
        }

        public void actionPerformed(ActionEvent e) {
            if (node.getNetwork() != null) {
                node.getNetwork().removeChild(node);
                updateView();
            }
        }
    }

    private class SetRenderedNodeAction extends AbstractAction {
        private Node node;

        public SetRenderedNodeAction(Node node) {
            super("Set Rendered");
            this.node = node;
        }

        public void actionPerformed(ActionEvent e) {
            node.getNetwork().setRenderedNode(node);
        }
    }
}
