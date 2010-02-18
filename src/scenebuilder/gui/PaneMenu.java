package scenebuilder.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;


public class PaneMenu extends JComponent implements MouseListener {
    private static final Font SMALL_BOLD_FONT = new Font(Font.DIALOG, Font.BOLD, 11);
    public static final Color TEXT_NORMAL_COLOR = new Color(60, 60, 60);
    private static Image paneMenuLeft, paneMenuBackground, paneMenuRight;

    static {
        try {
            paneMenuLeft = ImageIO.read(new File("res/pane-menu-left.png"));
            paneMenuBackground = ImageIO.read(new File("res/pane-menu-background.png"));
            paneMenuRight = ImageIO.read(new File("res/pane-menu-right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PaneMenu() {
        Dimension d = new Dimension(103, 21);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
        addMouseListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // Full width minus left side and right side
        int contentWidth = getWidth() - 9 - 21;
        g.drawImage(paneMenuLeft, 0, 0, null);
        g.drawImage(paneMenuBackground, 9, 0, contentWidth, 21, null);
        g.drawImage(paneMenuRight, 9 + contentWidth, 0, null);

        g2.setFont(SMALL_BOLD_FONT);
        g2.setColor(TEXT_NORMAL_COLOR);
        SwingUtils.drawShadowText(g2, getMenuName(), 9, 14);
    }

    public String getMenuName() {
        return "";
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

}
