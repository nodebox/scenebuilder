package nodebox.app;

import nodebox.node.Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AddressBar extends JPanel implements MouseListener, ExceptionListener {


    private static BufferedImage addressGradient, addressArrow, addressExclamation;
    private static TexturePaint addressGradientPaint;

    private static final Font SMALL_BOLD_FONT = Theme.INFO_FONT;
    public static final Color TEXT_NORMAL_COLOR = new Color(60, 60, 60);
    public static final Color TEXT_ARMED_COLOR = new Color(0, 0, 0);
    private static final int ADDRESS_BAR_HEIGHT = 25;

    static {
        addressGradient = PlatformUtils.loadImageResource("address-gradient.png");
        addressGradientPaint = new TexturePaint(addressGradient, new Rectangle2D.Float(0, 0, addressGradient.getWidth(null), addressGradient.getHeight(null)));
        addressArrow = PlatformUtils.loadImageResource("address-arrow.png");
        addressExclamation = PlatformUtils.loadImageResource("address-exclamation.png");
    }

    //private ArrayList<Node> parts = new List<Node>[]{"root", "poster", "background"};
    private int[] positions;
    private int armed = -1;

    private SceneDocument document;
    private Exception renderException;

    public AddressBar(SceneDocument document) {
        this.document = document;
        document.getRenderer().addExceptionListener(this);
        addMouseListener(this);
        setMinimumSize(new Dimension(0, ADDRESS_BAR_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, ADDRESS_BAR_HEIGHT));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, ADDRESS_BAR_HEIGHT));
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

        g2.setPaint(addressGradientPaint);
        g2.fill(g2.getClipBounds());
        //g2.drawImage(addressGradient, 0, 0, getWidth(), 26, null);

        int x = 14;

        for (int i = 0; i < networks.size(); i++) {
            Network part = networks.get(i);
            if (i == armed) {
                g2.setColor(TEXT_ARMED_COLOR);
            } else {
                g2.setColor(TEXT_NORMAL_COLOR);
            }
            String displayName = part.getDisplayName();
            SwingUtils.drawShadowText(g2, displayName, x, 16);

            int width = (int) g2.getFontMetrics().stringWidth(displayName);
            x += width + 5;
            positions[i] = x + 10;
            g2.drawImage(addressArrow, x, 0, null);
            x += 15;
        }

        String version = Application.getInstance().getVersion();
        int versionX = getWidth() - g2.getFontMetrics().stringWidth(version) - 10;
        SwingUtils.drawShadowText(g2, version, versionX, 16);

        if (renderException != null) {
            g2.drawImage(addressExclamation, versionX - 30, 0, null);
        }

    }

    // TODO: Check if the mouse was clicked on the right side and show ExceptionDialog(renderException)

    public void mouseClicked(MouseEvent e) {
        if (renderException != null) {
            int mx = e.getX();
            if (mx > getWidth() - 100) {
                ExceptionDialog dialog = new ExceptionDialog(null, renderException);
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        }
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

    public void exceptionThrown(Exception e) {
        boolean hadRenderException = renderException != null;
        renderException = e;
        if (!hadRenderException) {
            repaint();
        }
        setToolTipText(e.toString());
    }

    public void exceptionCleared() {
        boolean hadRenderException = renderException != null;
        renderException = null;
        if (hadRenderException) {
            repaint();
        }
        setToolTipText("");
    }
}
