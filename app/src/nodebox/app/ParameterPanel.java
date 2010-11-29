package nodebox.app;

import nodebox.node.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ParameterPanel extends JPanel implements PropertyChangeListener, ActionListener, ChangeListener {

    public static final int LABEL_WIDTH = 114;

    private static final Font NODE_LABEL_FONT = Theme.SMALL_BOLD_FONT;
    private static final Font PORT_LABEL_FONT = Theme.INFO_FONT;
    private static final Font PORT_VALUE_FONT = Theme.INFO_FONT;
    private static BufferedImage backgroundImage;
    private static TexturePaint backgroundPaint;

    static {
        backgroundImage = PlatformUtils.loadImageResource("parameter-background.png");
        backgroundPaint = new TexturePaint(backgroundImage, new Rectangle(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null)));
    }

    private Map<JComponent, Port> components = new HashMap<JComponent, Port>();
    private SceneDocument document;
    private Set<Node> selection;

    public ParameterPanel(SceneDocument document) {
        this.document = document;
        Dimension d = new Dimension(300, 100);
        //setSize(d.width, d.height);
        setMinimumSize(d);
        setPreferredSize(d);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.DARK_GRAY);
        Timer updateValuesTimer = new Timer(100, new UpdateValues());
        updateValuesTimer.start();
    }

    /**
     * Listen to selection change events from the NetworkViewer.
     *
     * @param evt
     */
    public void propertyChange(PropertyChangeEvent evt) {
        selection = (Set<Node>) evt.getNewValue();
        clearInterface();
        for (Node node : selection) {
            makeInterfaceForNode(node);
        }
        finishInterface();
    }


    private void finishInterface() {
        add(Box.createVerticalGlue());
        repaint();
    }

    private void clearInterface() {
        removeAll();
        components.clear();
    }

    private void forceSize(JComponent c, int width, int height) {
        Dimension d = new Dimension(width, height);
        c.setPreferredSize(d);
        c.setMaximumSize(d);
        c.setMinimumSize(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle clip = g2.getClipBounds();
        g2.setPaint(backgroundPaint);
        g2.fill(clip);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
    }

    private void makeInterfaceForNode(Node node) {
        JLabel nodeNameLabel = new ShadowLabel(node.getName());
        forceSize(nodeNameLabel, 300, 20);
        nodeNameLabel.setHorizontalAlignment(JLabel.LEFT);
        add(nodeNameLabel);
        add(Box.createVerticalStrut(5));
        // Include custom node interface.
        JComponent customEditor = node.createCustomEditor();
        if (customEditor != null) {
            add(customEditor);
            add(Box.createVerticalStrut(5));
        }
        // Include all ports
        for (Port port : node.getInputPorts()) {
            JPanel portRow = new JPanel(new BorderLayout());
            portRow.setOpaque(false);
            portRow.setLayout(new BoxLayout(portRow, BoxLayout.X_AXIS));
            forceSize(portRow, 300, 20);
            JLabel portLabel = new ShadowLabel(port.getAttribute(Port.DISPLAY_NAME_ATTRIBUTE).toString());
            forceSize(portLabel, LABEL_WIDTH, 20);
            portRow.add(portLabel);
            portRow.add(Box.createHorizontalStrut(5));
            JComponent c = getWidgetForPort(port);
            if (c == null)
                continue;

            if (port.isConnected()) {
                c.setEnabled(false);
            }
            components.put(c, port);
            portRow.add(c);
            add(portRow);
            add(Box.createVerticalStrut(5));
        }
        add(Box.createVerticalGlue());
        validate();
        repaint();
    }

    private JComponent getWidgetForPort(Port port) {
        String widget = port.getWidget();
        if (widget == null) {
            return null;
        }

        Object value = port.getValue();
        if (widget.equals("boolean")) {
            JCheckBox checkBox = new JCheckBox("", (Boolean) value);
            checkBox.setOpaque(false);
            checkBox.putClientProperty("JComponent.sizeVariant", "small");
            checkBox.addActionListener(this);
            return checkBox;
        } else if (widget.equals("float")) {
            DraggableNumber number = new DraggableNumber();
            number.setValue((Float) value);
            number.addChangeListener(this);
            return number;
        } else if (widget.equals("int")) {
            DraggableNumber number = new DraggableNumber();
            number.setValue((Integer) value);
            number.addChangeListener(this);
            NumberFormat intFormat = NumberFormat.getNumberInstance();
            intFormat.setMinimumFractionDigits(0);
            intFormat.setMaximumFractionDigits(0);
            number.setNumberFormat(intFormat);
            return number;
        } else if (widget.equals("menu")) {
            String selectedValue = ((PersistablePort) port).getValueAsString();
            MenuItem selectedItem = null;
            Vector<MenuItem> v = new Vector<MenuItem>();
            for (Map.Entry<String, String> entry : ((MenuPort) port).getMenuItems().entrySet()) {
                MenuItem item = new MenuItem(entry.getKey(), entry.getValue());
                v.add(item);
                if (entry.getKey().equals(selectedValue))
                    selectedItem = item;
            }
            JComboBox menu = new JComboBox(v);
            menu.setSelectedItem(selectedItem);
            menu.addActionListener(this);
            return menu;
        } else if (widget.equals("color")) {
            ColorWell well = new ColorWell();
            well.setColor((Color) value);
            well.addChangeListener(this);
            return well;
        } else if (widget.equals("string")) {
            JTextField field = new JTextField((String) value);
            field.setFont(PORT_VALUE_FONT);
            field.putClientProperty("JComponent.sizeVariant", "small");
            field.addActionListener(this);
            return field;
        } else {
            return null;
        }
    }

    private void updateValuesForNode(Node node) {
        for (Map.Entry<JComponent, Port> entry : components.entrySet()) {
            JComponent c = entry.getKey();
            Port port = entry.getValue();
            if (!port.isConnected()) continue;
            if (c instanceof JCheckBox) {
                ((JCheckBox) c).setSelected((Boolean) port.getValue());
            } else if (c instanceof DraggableNumber) {
                Object value = port.getValue();
                if (value instanceof Float) {
                    ((DraggableNumber) c).setValue((Float) value);
                } else if (value instanceof Integer) {
                    ((DraggableNumber) c).setValue((Integer) value);
                }
            } else if (c instanceof ColorWell) {
                ((ColorWell) c).setColor((Color) port.getValue());
            } else if (c instanceof JTextField) {
                ((JTextField) c).setText((String) port.getValue());
            }
        }
    }

    private Port getPort(JComponent c) {
        return components.get(c);
    }


    public void actionPerformed(ActionEvent e) {
        Port p = getPort((JComponent) e.getSource());
        if (p == null) return;
        if (p.getWidget().equals("boolean")) {
            JCheckBox b = (JCheckBox) e.getSource();
            p.setValue(b.isSelected());
        } else if (p.getWidget().equals("string")) {
            JTextField f = (JTextField) e.getSource();
            p.setValue(f.getText());
        } else if (p.getWidget().equals("menu")) {
            JComboBox b = (JComboBox) e.getSource();
            MenuItem item = (MenuItem) b.getSelectedItem();
            p.setValue(((PersistablePort) p).parseValue(item.key));
        }
    }

    public void stateChanged(ChangeEvent e) {
        Port p = getPort((JComponent) e.getSource());
        if (p == null) return;
        if (p.getWidget().equals("float")) {
            DraggableNumber number = (DraggableNumber) e.getSource();
            p.setValue((float) number.getValue());
        } else if (p.getWidget().equals("int")) {
            DraggableNumber number = (DraggableNumber) e.getSource();
            p.setValue((int) Math.round(number.getValue()));
        } else if (p.getWidget().equals("color")) {
            ColorWell well = (ColorWell) e.getSource();
            p.setValue(well.getColor());
        }
    }


    private class UpdateValues implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selection == null) return;
            for (Node node : selection) {
                updateValuesForNode(node);
            }
        }
    }

    private class MenuItem {
        private String key;
        private String label;

        private MenuItem(String key, String label) {
            this.key = key;
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

}
