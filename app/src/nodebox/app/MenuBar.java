package nodebox.app;

import nodebox.app.actions.*;
import nodebox.node.Node;
import nodebox.node.NodeInfo;
import nodebox.node.NodeManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MenuBar extends JMenuBar {

    private SceneDocument document;
    private NodeManager manager;
    private static ArrayList<JMenu> recentFileMenus = new ArrayList<JMenu>();
    private static Preferences recentFilesPreferences = Preferences.userRoot().node("/nodebox/recent");
    private static Logger logger = Logger.getLogger(MenuBar.class.getName());

    public MenuBar() {
        this(null, null);
    }

    public MenuBar(SceneDocument document, NodeManager manager) {
        this.document = document;
        // File menu
        JMenu fileMenu = createMenu("File");
        fileMenu.add(new NewDocumentAction());
        fileMenu.add(new OpenDocumentAction());
        JMenu recentFileMenu = createMenu("Open Recent");
        recentFileMenus.add(recentFileMenu);
        buildRecentFileMenu();
        fileMenu.add(recentFileMenu);
        fileMenu.addSeparator();
        fileMenu.add(new CloseDocumentAction(document));
        fileMenu.add(new SaveDocumentAction(document));
        fileMenu.add(new SaveAsDocumentAction(document));
        fileMenu.addSeparator();
        fileMenu.add(new ScenePropertiesAction(document));
        fileMenu.addSeparator();
        fileMenu.add(new NodeLibrariesAction());
        if (!PlatformUtils.isMac()) {
            fileMenu.addSeparator();
            fileMenu.add(new ExitAction());
        }
        add(fileMenu);

        // Edit menu
        JMenu editMenu = createMenu("Edit");
//        editMenu.add(new UndoAction());
//        editMenu.add(new RedoAction());
        editMenu.addSeparator();
//        editMenu.add(new CutAction());
//        editMenu.add(new CopyAction());
//        editMenu.add(new PasteAction());
        editMenu.addSeparator();
//        editMenu.add(new DeleteAction());
        add(editMenu);

        // Node menu
/*        JMenu createMenu = createMenu("Create");
        if (manager != null) {
            for (String category : manager.getNodeCategories()) {
                JMenu categoryMenu = createMenu(category);
                for (NodeInfo nodeInfo: manager.getNodeInfoList(category)) {
                    categoryMenu.add(new CreateNodeAction(document, nodeInfo));
                }
                createMenu.add(categoryMenu);
            }
        }
        add(createMenu); */
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        return menu;
    }

    public SceneDocument getDocument() {
        return document;
    }

    public boolean isEnabled() {
        return document != null;
    }

    public static void addRecentFile(File f) {
        File canonicalFile;
        try {
            canonicalFile = f.getCanonicalFile();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not get canonical file name", e);
            return;
        }
        ArrayList<File> fileList = getRecentFiles();
        // If the recent file was already in the list, remove it and add it to the top.
        // If the list did not contain the file, the remove call does nothing.
        fileList.remove(canonicalFile);
        fileList.add(0, canonicalFile);
        writeRecentFiles(fileList);
        buildRecentFileMenu();
    }

    public static void writeRecentFiles(ArrayList<File> fileList) {
        int i = 1;
        for (File f : fileList) {
            try {
                recentFilesPreferences.put(String.valueOf(i), f.getCanonicalPath());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not get canonical file name", e);
                return;
            }
            i++;
            if (i > 10) break;
        }
        try {
            recentFilesPreferences.flush();
        } catch (BackingStoreException e) {
            logger.log(Level.WARNING, "Could not write recent files preferences", e);
        }
    }

    public static ArrayList<File> getRecentFiles() {
        ArrayList<File> fileList = new ArrayList<File>(10);
        for (int i = 1; i <= 10; i++) {
            File file = new File(recentFilesPreferences.get(String.valueOf(i), ""));
            if (file.exists()) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    private static void buildRecentFileMenu() {
        for (JMenu recentFileMenu : recentFileMenus) {
            recentFileMenu.removeAll();
            for (File f : getRecentFiles()) {
                recentFileMenu.add(new OpenRecentDocumentAction(f));
            }
        }
    }

}
