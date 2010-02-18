package scenebuilder.editor;

import scenebuilder.gui.ColorWell;
import scenebuilder.gui.DraggableNumber;
import scenebuilder.model.Node;
import scenebuilder.model.Port;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParameterPanel extends JPanel implements PropertyChangeListener, ActionListener, ChangeListener {

    private static final Font NODE_LABEL_FONT = new Font(Font.DIALOG, Font.BOLD, 11);
    private static final Font PORT_LABEL_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);
    private static final Font PORT_VALUE_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);


    private Map<JComponent, Port> components = new HashMap<JComponent, Port>();

    public ParameterPanel() {
        Dimension d = new Dimension(300, 600);
        //setSize(d.width, d.height);
        setMinimumSize(d);
        setPreferredSize(d);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Listen to selection change events from the SceneViewer.
     *
     * @param evt
     */
    public void propertyChange(PropertyChangeEvent evt) {
        Set<Node> selection = (Set<Node>) evt.getNewValue();
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
            if (port.getType() == Port.Type.NUMBER) {
                DraggableNumber number = new DraggableNumber();
                number.setValue(node.asNumber(port.getName()));
                number.addChangeListener(this);
                c = number;
            } else if (port.getType() == Port.Type.COLOR) {
                ColorWell well = new ColorWell();
                well.setColor(node.asColor(port.getName()));
                well.addChangeListener(this);
                c = well;
            } else {
                JTextField field = new JTextField(valueString);
                field.setFont(PORT_VALUE_FONT);
                field.putClientProperty("JComponent.sizeVariant", "small");
                field.addActionListener(this);
                c = field;
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

    private Port getPort(JComponent c) {
        return components.get(c);
    }


    public void actionPerformed(ActionEvent e) {
        JTextField f = (JTextField) e.getSource();
        Port p = getPort(f);
        if (p == null) return;
        Node n = p.getNode();
        Object parsedValue = n.parseValue(p.getName(), f.getText());
        p.getNode().setValue(p.getName(), parsedValue);
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof DraggableNumber) {
            DraggableNumber number = (DraggableNumber) e.getSource();
            Port p = getPort(number);
            if (p == null) return;
            p.setValue(number.getValue());
        } else if (e.getSource() instanceof ColorWell) {
            ColorWell well = (ColorWell) e.getSource();
            Port p = getPort(well);
            if (p == null) return;
            p.setValue(well.getColor());
        }
    }
}
