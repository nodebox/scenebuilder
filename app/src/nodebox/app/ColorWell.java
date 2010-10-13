package nodebox.app;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorWell extends JComponent implements ChangeListener {

    public static final Color PARAMETER_LABEL_BACKGROUND = new Color(153, 153, 153);

    private static final int ALPHA_BAR_HEIGHT = 3;
    private static final Color ALPHA_BAR_COLOR = new Color(255, 255, 255, 70);

    private Color color = Color.LIGHT_GRAY;

    /**
     * Only one <code>ChangeEvent</code> is needed per color well
     * instance since the event's only state is the source property.
     * The source of events generated is always "this".
     */
    private transient ChangeEvent changeEvent;

    public ColorWell() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showColorDialog();
            }
        });
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
        r.width -= 1;
        r.height -= 1;
        if (isEnabled()) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(PARAMETER_LABEL_BACKGROUND);
        }
        g.fillRect(r.x, r.y, r.width, r.height);
        if (!isEnabled()) {
            r.grow(-5, -5);
        }

        Color colorWithoutAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue());
        g.setColor(colorWithoutAlpha);
        g.fillRect(r.x, r.y, r.width, r.height - ALPHA_BAR_HEIGHT);
        float a = color.getAlpha() / 255f;
        g.setColor(ALPHA_BAR_COLOR);
        g.fillRect(r.x, r.y + r.height - ALPHA_BAR_HEIGHT, (int) (r.width * a), ALPHA_BAR_HEIGHT);

    }

    public void showColorDialog() {
        ColorDialog dialog = new ColorDialog((Frame) SwingUtilities.getWindowAncestor(this));
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

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the button.
     *
     * @param l the listener to be removed
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    private void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        ColorDialog dialog = (ColorDialog) e.getSource();
        color = dialog.getColor();
        fireStateChanged();
        repaint();
    }
}