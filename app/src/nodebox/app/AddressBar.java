package nodebox.app;

import nodebox.node.Network;
import nodebox.node.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddressBar extends JPanel implements MouseListener {


    public static Image addressGradient;
    public static Image addressArrow;

    private static final Font SMALL_BOLD_FONT = new Font(Font.DIALOG, Font.BOLD, 11);
    public static final Color TEXT_NORMAL_COLOR = new Color(60, 60, 60);
    public static final Color TEXT_ARMED_COLOR = new Color(0, 0, 0);

    static {
        try {
            addressGradient = ImageIO.read(new File("app/res/address-gradient.png"));
            addressArrow = ImageIO.read(new File("app/res/address-arrow.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //private ArrayList<Node> parts = new List<Node>[]{"root", "poster", "background"};
    private int[] positions;
    private int armed = -1;

    private SceneDocument document;

    public AddressBar(SceneDocument document) {
        this.document = document;
        addMouseListener(this);
        setMinimumSize(new Dimension(0, 25));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 25));
        setLayout(null);
    }

    private Network getCurrentNetwork() {
        return document.getCurrentNetwork();
    }

    private java.util.List<Network> getNetworkParts() {
        ArrayList<Network> parts = new ArrayList<Network>();
        if (getCurrentNetwork() == null) return parts;
        Network currentNetwork = getCurrentNetwork();
        parts.add(0, currentNetwork);
        while (currentNetwork.getNetwork() != null) {
            parts.add(0, currentNetwork.getNetwork());
            currentNetwork = currentNetwork.getNetwork();
        }
        return parts;
    }

    @Override
    protected void paintComponent(Graphics g) {
        java.util.List<Network> networks = getNetworkParts();
        positions = new int[networks.size()];
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(SMALL_BOLD_FONT);

        g2.drawImage(addressGradient, 0, 0, getWidth(), 25, null);

        int x = 14;

        for (int i = 0; i < networks.size(); i++) {
            Network part = networks.get(i);
            if (i == armed) {
                g2.setColor(TEXT_ARMED_COLOR);
            } else {
                g2.setColor(TEXT_NORMAL_COLOR);
            }
            String displayName = part.getAttribute(Node.DISPLAY_NAME_ATTRIBUTE).toString();
            SwingUtils.drawShadowText(g2, displayName, x, 16);

            int width = (int) g2.getFontMetrics().stringWidth(displayName);
            x += width + 5;
            positions[i] = x + 10;
            g2.drawImage(addressArrow, x, 1, null);
            x += 15;
        }

        String version = Application.getInstance().getVersion();

        SwingUtils.drawShadowText(g2, version, getWidth() - g2.getFontMetrics().stringWidth(version) - 10, 16);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        armed = partIndex(mx);
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        armed = -1;
        int mx = e.getX();
        int partIndex = partIndex(mx);
        if (partIndex == -1) return;
        java.util.List<Network> networks = getNetworkParts();
        Network selectedNetwork = networks.get(partIndex);
        //System.out.println("part = " + selectedNetwork);
        if (selectedNetwork != null)
            document.setCurrentNetwork(selectedNetwork);
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        armed = -1;
        repaint();
    }


    private int partIndex(int x) {
        if (positions == null) return -1;
        for (int i = 0; i < positions.length; i++) {
            if (x < positions[i])
                return i;
        }
        return -1;
    }
}
