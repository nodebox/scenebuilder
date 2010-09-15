package nodebox.app;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorWell extends JButton implements ActionListener, ChangeListener {

    public static final Color PARAMETER_LABEL_BACKGROUND = new Color(153, 153, 153);

    private Color color = Color.LIGHT_GRAY;

    public ColorWell() {
        addActionListener(this);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle r = g.getClipBounds();
        if (isEnabled()) {
            g.setColor(Color.darkGray);
        } else {
            g.setColor(PARAMETER_LABEL_BACKGROUND);
        }
        g.fillRect(r.x, r.y, r.width - 1, r.height - 1);
        if (isEnabled()) {
            r.grow(1, 1);
        } else {
            r.grow(-5, -5);
        }
        g.setColor(color);
        g.fillRect(r.x, r.y, r.width - 1, r.height - 1);
    }

    public void actionPerformed(ActionEvent e) {
        ColorDialog dialog = new ColorDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setColor(color);
        dialog.setMinimumSize(new Dimension(400, 375));
        dialog.setPreferredSize(new Dimension(540, 375));
        dialog.setMaximumSize(new Dimension(Integer.MAX_VALUE, 375));
        dialog.setSize(540, 375);
        dialog.addChangeListener(this);
        dialog.setAlwaysOnTop(true);
        SwingUtils.centerOnScreen(dialog);
        dialog.setVisible(true);

    }

    public void stateChanged(ChangeEvent e) {
        ColorDialog dialog = (ColorDialog) e.getSource();
        color = dialog.getColor();
        fireStateChanged();
    }
}