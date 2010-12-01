package nodebox.graphics.nodes;

import nodebox.graphics.Geometry;
import nodebox.node.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.*;
import java.util.List;

@Description("Merges multiple shapes together.")
@Drawable
@Category("Geometry")
public class MergeNode extends GeneratorNode {
    private static java.util.List<String> customKeys;

    private int numPorts = 5;
    private Map<String, GeometryPort> geometryPortMap = new HashMap<String, GeometryPort>();

    public MergeNode() {
        customKeys = new LinkedList<String>();
        customKeys.add("ports");
        setPorts();
    }
    
    @Override
    public Geometry cook(Context context, float time) {
        Geometry g = new Geometry();
        for (Port port : getInputPorts()) {
            GeometryPort gPort = (GeometryPort) port;
            if (gPort.get() != null)
            g.extend(gPort.get());
        }
        return g;
    }

    private void setPorts() {
        Map<String, GeometryPort> oldGeometryPortMap = geometryPortMap;
        geometryPortMap = new HashMap<String, GeometryPort>();
        for (int i = 0; i < numPorts; i++) {
            String name = "geo" + (i + 1);
            GeometryPort p;
            if (oldGeometryPortMap.containsKey(name)) {
                p = oldGeometryPortMap.get(name);
                oldGeometryPortMap.remove(name);
            } else {
                p = new GeometryPort(this, name, Port.Direction.INPUT);
            }
            geometryPortMap.put(name, p);
        }
        // Remove all old ports that remain.
        for (GeometryPort p : oldGeometryPortMap.values()) {
            removePort(p);
        }
    }


    @Override
    public JComponent createCustomEditor() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        Dimension d = new Dimension(300, 20);
        panel.setPreferredSize(d);
        panel.setMaximumSize(d);
        panel.setMinimumSize(d);

        d = new Dimension(114, 20);
        JLabel label = new JLabel("Shapes");
        label.setPreferredSize(d);
        label.setMaximumSize(d);
        label.setMinimumSize(d);
        label.setForeground(new Color(255, 255, 0, 120));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(label);

        panel.add(Box.createHorizontalStrut(5));

        final JSpinner s = new JSpinner(new SpinnerNumberModel(numPorts, 0, 20, 1));
        s.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent e) {
               portsNumberChanged((Integer) s.getValue());
           }
        });
        panel.add(s);
        return panel;
    }

    private void portsNumberChanged(int numPorts) {
        this.numPorts = numPorts;
        setPorts();
    }

    @Override
    public List<String> getCustomKeys() {
        return customKeys;
    }

    @Override
    public String serializeCustomValue(String key) {
        if (key.equals("ports")) {
            return Integer.toString(numPorts);
        } else {
            return null;
        }
    }

    @Override
    public void deserializeCustomValue(String key, String value) {
        if (key.equals("ports")) {
            numPorts = Integer.parseInt(value);
            setPorts();
        } else {
            super.deserializeCustomValue(key, value);
        }
    }
}
