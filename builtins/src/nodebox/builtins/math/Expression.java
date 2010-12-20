package nodebox.builtins.math;

import nodebox.node.*;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Description("Evaluate a mathematical expression.")
@Category("Math")
public class Expression extends Node {

    private static ParserContext parserContext = new ParserContext();

    static {
        // Add "built-in" methods to the expression context.
        parserContext = new ParserContext();
        for (Method m : Math.class.getMethods()) {
            parserContext.addImport(m.getName(), m);
        }
    }

    private static Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]+");

    private static List<String> customKeys;

    private String expression = "a + b + c";
    private Serializable compiledExpression;
    private Map<String, FloatPort> expressionPortMap = new HashMap<String, FloatPort>();

    public FloatPort pResult = new FloatPort(this, "result", Port.Direction.OUTPUT);

    public Expression() {
        customKeys = new LinkedList<String>();
        customKeys.add("expression");
        parseExpression();
    }

    public void execute(Context context, float time) {
        Map<String, Float> valueMap = new HashMap<String, Float>();
        for (Map.Entry<String, FloatPort> portEntry : expressionPortMap.entrySet()) {
            valueMap.put(portEntry.getKey(), portEntry.getValue().get());
        }
        if (compiledExpression != null) {
            Object result = null;
            try {
                result = MVEL.executeExpression(compiledExpression, valueMap);
            if (result instanceof Float) {
                pResult.set((Float) result);
            } else if (result instanceof Double) {
                pResult.set(((Double) result).floatValue());
            } else if (result instanceof Integer) {
                pResult.set(((Integer) result).floatValue());
            } else if (result instanceof Long) {
                pResult.set(((Long) result).floatValue());
            } else {
                pResult.set(0);
            }
            } catch (Exception e) {
                pResult.set(0);
            }
        } else {
            pResult.set(0);
        }
    }

    private void parseExpression() {
        Map<String, FloatPort> oldExpressionPortMap = expressionPortMap;
        expressionPortMap = new HashMap<String, FloatPort>();
        Matcher m = NAME_PATTERN.matcher(expression);
        while (m.find()) {
            String name = m.group();
            // Names that are reserved variables are skipped.
            if (!parserContext.hasImport(name)) {
                FloatPort p;
                if (oldExpressionPortMap.containsKey(name)) {
                    p = oldExpressionPortMap.get(name);
                    oldExpressionPortMap.remove(name);
                } else {
                    p = new FloatPort(this, name, Port.Direction.INPUT);
                }
                expressionPortMap.put(name, p);
            }
        }
        // Remove all old ports that remain.
        for (FloatPort p : oldExpressionPortMap.values()) {
            removePort(p);
        }
        try {
            this.compiledExpression = MVEL.compileExpression(expression, parserContext);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            this.compiledExpression = null;
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
        JLabel label = new JLabel("Expression");
        label.setPreferredSize(d);
        label.setMaximumSize(d);
        label.setMinimumSize(d);
        label.setForeground(new Color(255, 255, 0, 120));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(label);

        panel.add(Box.createHorizontalStrut(5));

        final JTextField f = new JTextField(expression);
        f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    expressionChanged(f.getText());
                }
            }

        });
        panel.add(f);
        return panel;
    }

    private void expressionChanged(String expression) {
        this.expression = expression;
        parseExpression();
    }

    @Override
    public List<String> getCustomKeys() {
        return customKeys;
    }

    @Override
    public String serializeCustomValue(String key) {
        if (key.equals("expression")) {
            return expression;
        } else {
            return null;
        }
    }

    @Override
    public void deserializeCustomValue(String key, String value) {
        if (key.equals("expression")) {
            expression = value;
            parseExpression();
        } else {
            super.deserializeCustomValue(key, value);
        }
    }
}
