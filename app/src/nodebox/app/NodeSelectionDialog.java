package nodebox.app;

import nodebox.node.NodeInfo;
import nodebox.node.NodeManager;
import nodebox.util.Strings;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;

public class NodeSelectionDialog extends JDialog {
    
    private class FilteredNodeInfoListModel implements ListModel {
        private NodeManager manager;
        private java.util.List<NodeInfo> nodeInfoList;
        private String searchString;

        private FilteredNodeInfoListModel(NodeManager manager) {
            this.manager = manager;
            this.searchString = "";
            nodeInfoList = new java.util.ArrayList<NodeInfo>();
            for (String category : manager.getNodeCategories()) {
                nodeInfoList.addAll(manager.getNodeInfoList(category));
            }
            Collections.sort(nodeInfoList, NodeInfo.ALPHABETIC_COMPARATOR);
        }

        public String getSearchString() {
            return searchString;
        }

        public void setSearchString(String searchString) {
            this.searchString = searchString.trim();
            nodeInfoList = new java.util.ArrayList<NodeInfo>();
            if (this.searchString.length() == 0) {
                for (String category : manager.getNodeCategories()) {
                    nodeInfoList.addAll(manager.getNodeInfoList(category));
                }
            } else {
                for (String category : manager.getNodeCategories()) {
                    for (NodeInfo info : manager.getNodeInfoList(category))
                        if (contains(info, this.searchString))
                            nodeInfoList.add(info);
                }
            }
            Collections.sort(nodeInfoList, NodeInfo.ALPHABETIC_COMPARATOR);
        }

        private boolean contains(NodeInfo nodeInfo, String searchString) {
            String description = nodeInfo.getDescription() == null ? "" : nodeInfo.getDescription().toLowerCase();
            String category = nodeInfo.getCategory() == null ? "" : nodeInfo.getCategory().toLowerCase();
            String nodeClassName = Strings.humanizeName(nodeInfo.getNodeClass().getSimpleName()).toLowerCase();
            String s = searchString.toLowerCase();
            return nodeClassName.contains(s) || description.contains(s) || category.contains(s);
        }

        public int getSize() {
            return nodeInfoList.size();
        }

        public Object getElementAt(int index) {
            return nodeInfoList.get(index);
        }

        public void addListDataListener(ListDataListener l) {
            // The list is immutable; don't listen.
        }

        public void removeListDataListener(ListDataListener l) {
            // The list is immutable; don't listen.
        }

    }

    private class NodeInfoRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            assert (value instanceof NodeInfo);
            NodeInfo info = (NodeInfo) value;
            String html = "<html><b>" + Strings.humanizeName(info.getNodeClass().getSimpleName()) + "</b> - " + info.getDescription() + "<br><font color=#333333>" + info.getCategory() + "</font></html>";
            setText(html);
            if (isSelected) {
                setBackground(Theme.NODE_SELECTION_ACTIVE_BACKGROUND_COLOR);
            } else {
                setBackground(Theme.NODE_SELECTION_BACKGROUND_COLOR);
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
//            setIcon(new ImageIcon(NodeView.getImageForNode(node)));
            setBorder(Theme.BOTTOM_BORDER);
            setOpaque(true);
            return this;
        }
    }

    private NodeManager manager;
    private JTextField searchField;
    private JList nodeInfoList;
    private NodeInfo selectedNodeInfo;
    private FilteredNodeInfoListModel filteredNodeInfoListModel;
                                 
    public NodeSelectionDialog(NodeManager manager) {
        this(null, manager);
    }

    public NodeSelectionDialog(Frame owner, NodeManager manager) {
        super(owner, "New Node", true);
        getRootPane().putClientProperty("Window.style", "small");
        JPanel panel = new JPanel(new BorderLayout());
        this.manager = manager;
        filteredNodeInfoListModel = new FilteredNodeInfoListModel(manager);
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.variant", "search");
        EscapeListener escapeListener = new EscapeListener();
        searchField.addKeyListener(escapeListener);
        ArrowKeysListener arrowKeysListener = new ArrowKeysListener();
        searchField.addKeyListener(arrowKeysListener);
        SearchFieldChangeListener searchFieldChangeListener = new SearchFieldChangeListener();
        searchField.getDocument().addDocumentListener(searchFieldChangeListener);
        nodeInfoList = new JList(filteredNodeInfoListModel);
        nodeInfoList.setSelectedIndex(0);
        DoubleClickListener doubleClickListener = new DoubleClickListener();
        nodeInfoList.addMouseListener(doubleClickListener);
        nodeInfoList.addKeyListener(escapeListener);
        nodeInfoList.setCellRenderer(new NodeInfoRenderer());
        JScrollPane nodeInfoScroll = new JScrollPane(nodeInfoList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        nodeInfoScroll.setBorder(null);
        panel.add(searchField, BorderLayout.NORTH);
        panel.add(nodeInfoScroll, BorderLayout.CENTER);
        setContentPane(panel);
        setSize(500, 400);
        SwingUtils.centerOnScreen(this);
    }

    public NodeInfo getSelectedNodeInfo() {
        return selectedNodeInfo;
    }

    public NodeManager getManager() {
        return manager;
    }

    private void closeDialog() {
        setVisible(false);
    }

    private void moveUp() {
        int index = nodeInfoList.getSelectedIndex();
        index--;
        if (index < 0) {
            index = nodeInfoList.getModel().getSize() - 1;
        }
        nodeInfoList.setSelectedIndex(index);
    }

    private void moveDown() {
        int index = nodeInfoList.getSelectedIndex();
        index++;
        if (index >= nodeInfoList.getModel().getSize()) {
            index = 0;
        }
        nodeInfoList.setSelectedIndex(index);
    }

    private void selectAndClose() {
        selectedNodeInfo = ((NodeInfo) nodeInfoList.getSelectedValue());
        closeDialog();
    }

    private class DoubleClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                selectAndClose();
            }
        }
    }

    private class EscapeListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                closeDialog();
        }
    }

    private class ArrowKeysListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (e.getSource() == searchField)
                    moveUp();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (e.getSource() == searchField)
                    moveDown();
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                selectAndClose();
            }
        }
    }

    private class SearchFieldChangeListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            changedEvent();
        }

        public void removeUpdate(DocumentEvent e) {
            changedEvent();
        }

        public void changedUpdate(DocumentEvent e) {
            changedEvent();
        }

        private void changedEvent() {
            if (filteredNodeInfoListModel.getSearchString().equals(searchField.getText())) return;
            filteredNodeInfoListModel.setSearchString(searchField.getText());
            // Trigger a model reload.
            nodeInfoList.setModel(filteredNodeInfoListModel);
            nodeInfoList.setSelectedIndex(0);
            nodeInfoList.ensureIndexIsVisible(0);
            repaint();
        }
    }
}