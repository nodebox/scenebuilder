package nodebox.app;

import nodebox.node.Node;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class NodeLibraryManager extends JFrame {

    private BundleContext bundleContext;
    private JList libraryList;

    public NodeLibraryManager(BundleContext bundleContext) throws HeadlessException {
        super("Node Library Manager");
        this.bundleContext = bundleContext;
        libraryList = new JList(new LibraryListModel());
        libraryList.setCellRenderer(new LibraryCellRenderer());
        JScrollPane listScroll = new JScrollPane(libraryList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton installLibraryButton = new JButton("Install Node Library...");
        installLibraryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                installNodeLibrary();
            }
        });
        buttonPanel.add(installLibraryButton);
        mainPanel.add(listScroll, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
        setSize(800, 600);
    }

    private void installNodeLibrary() {
        File chosenFile = FileUtils.showOpenDialog(this, "", "jar", "JAR File");
        if (chosenFile != null) {
            try {
                Bundle bundle = bundleContext.installBundle("file:" + chosenFile.getAbsolutePath());
                Application.getInstance().loadNodeClassesFromBundle(bundle);
            } catch (BundleException e) {
                e.printStackTrace();
            }
            libraryList.setModel(new LibraryListModel());
            libraryList.repaint();
        }
    }

    private class LibraryListModel implements ListModel {

        public int getSize() {
            return bundleContext.getBundles().length;
        }

        public Object getElementAt(int i) {
            return bundleContext.getBundles()[i];
        }

        public void addListDataListener(ListDataListener listDataListener) {
        }

        public void removeListDataListener(ListDataListener listDataListener) {
        }
    }


    private class LibraryCellRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            assert (value instanceof Bundle);
            Bundle bundle = (Bundle) value;
            String html = "<html><b>" + bundle.getSymbolicName() + "</b> - " + bundle.getVersion() + "</html>";
            setText(html);
            if (isSelected) {
                setBackground(Theme.NODE_SELECTION_ACTIVE_BACKGROUND_COLOR);
            } else {
                setBackground(Theme.NODE_SELECTION_BACKGROUND_COLOR);
            }
            setFont(libraryList.getFont());
            //setIcon(new ImageIcon(NodeView.getImageForNode(node)));
            setBorder(Theme.BOTTOM_BORDER);
            setOpaque(true);
            return this;
        }

    }
}
