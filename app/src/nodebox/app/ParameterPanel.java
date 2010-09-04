package nodebox.app;

import nodebox.node.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class
        ParameterPanel extends JPanel implements PropertyChangeListener, ActionListener, ChangeListener {

    private static final Font NODE_LABEL_FONT = new Font(Font.DIALOG, Font.BOLD, 11);
    private static final Font PORT_LABEL_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);
    private static final Font PORT_VALUE_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);


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

    private void makeInterfaceForNode(Node node) {
        JLabel nodeNameLabel = new JLabel(node.getName());
        forceSize(nodeNameLabel, 300, 20);
        nodeNameLabel.setHorizontalAlignment(JLabel.LEFT);
        nodeNameLabel.setFont(NODE_LABEL_FONT);
        add(nodeNameLabel);
        add(Box.createVerticalStrut(5));
        for (Port port : node.getInputPorts()) {
            JPanel portRow = new JPanel(new BorderLayout());
            portRow.setLayout(new BoxLayout(portRow, BoxLayout.X_AXIS));
            forceSize(portRow, 300, 20);
            JLabel portLabel = new JLabel(port.getAttribute(Port.DISPLAY_NAME_ATTRIBUTE).toString());
            forceSize(portLabel, 100, 20);
            portLabel.setHorizontalAlignment(JLabel.RIGHT);
            portLabel.setFont(PORT_LABEL_FONT);
            portRow.add(portLabel);
            portRow.add(Box.createHorizontalStrut(5));
            Object value = port.getValue();
            String valueString = value == null ? "" : value.toString();

            JComponent c;
            if (port instanceof BooleanPort) {
                BooleanPort p = (BooleanPort) port;
                JCheckBox checkBox = new JCheckBox("", p.get());
                checkBox.putClientProperty("JComponent.sizeVariant", "small");
                checkBox.addActionListener(this);
                c = checkBox;
            } else if (port instanceof FloatPort) {
                FloatPort p = (FloatPort) port;
                DraggableNumber number = new DraggableNumber();
                number.setValue(p.get());
                number.addChangeListener(this);
                c = number;
            } else if (port instanceof IntPort) {
                IntPort p = (IntPort) port;
                DraggableNumber number = new DraggableNumber();
                number.setValue(p.get());
                number.addChangeListener(this);
                c = number;
                NumberFormat intFormat = NumberFormat.getNumberInstance();
                intFormat.setMinimumFractionDigits(0);
                intFormat.setMaximumFractionDigits(0);
                number.setNumberFormat(intFormat);
            } else if (port instanceof ColorPort) {
                ColorPort p = (ColorPort) port;
                ColorWell well = new ColorWell();
                well.setColor(p.get());
                well.addChangeListener(this);
                c = well;
            } else if (port instanceof StringPort) {
                StringPort p = (StringPort) port;
                JTextField field = new JTextField(p.get());
                field.setFont(PORT_VALUE_FONT);
                field.putClientProperty("JComponent.sizeVariant", "small");
                field.addActionListener(this);
                c = field;
            } else {
                continue;
            }
            if (port.isConnected()) {
                c.setEnabled(false);
            }
            forceSize(c, 200, 20);
            components.put(c, port);
            portRow.add(c);
            add(portRow);
            add(Box.createVerticalStrut(5));
        }
        JComponent c = node.createEditorComponent();
        if (c != null) {
            add(c);
        }
        add(Box.createVerticalStrut(10));
        validate();
        repaint();
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
        if (p instanceof BooleanPort) {
            JCheckBox b = (JCheckBox) e.getSource();
            p.setValue(b.isSelected());
        } else if (p instanceof StringPort) {
            JTextField f = (JTextField) e.getSource();
            p.setValue(f.getText());
        }
    }

    public void stateChanged(ChangeEvent e) {
        Port p = getPort((JComponent) e.getSource());
        if (p == null) return;
        if (p instanceof FloatPort) {
            DraggableNumber number = (DraggableNumber) e.getSource();
            p.setValue((float) number.getValue());
        } else if (p instanceof IntPort) {
            DraggableNumber number = (DraggableNumber) e.getSource();
            p.setValue((int) Math.round(number.getValue()));
        } else if (p instanceof ColorPort) {
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
}
