package nodebox.app;

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

public class FileUtils {
    public static File showOpenDialog(Frame owner, String pathName, String extensions, String description) {
        return showFileDialog(owner, pathName, extensions, description, FileDialog.LOAD);
    }

    public static File showSaveDialog(Frame owner, String pathName, String extensions, String description) {
        return showFileDialog(owner, pathName, extensions, description, FileDialog.SAVE);
    }

    private static File showFileDialog(Frame owner, String pathName, String extensions, String description, int fileDialogType) {
        FileDialog fileDialog = new FileDialog(owner, pathName, fileDialogType);
        if (pathName == null || pathName.trim().length() == 0) {
            SceneDocument document = SceneDocument.getCurrentDocument();
            if (document != null) {
                File documentFile = document.getDocumentFile();
                if (documentFile != null) {
                    fileDialog.setFile(documentFile.getParentFile().getPath());
                }
            }
        } else {
            File f = new File(pathName);
            if (f.isDirectory()) {
                fileDialog.setDirectory(pathName);
            } else {
                fileDialog.setFile(pathName);
            }
        }
        fileDialog.setFilenameFilter(new FileExtensionFilter(extensions, description));
        fileDialog.setVisible(true);
        String chosenFile = fileDialog.getFile();
        String dir = fileDialog.getDirectory();
        if (chosenFile != null) {
            return new File(dir + chosenFile);
        } else {
            return null;
        }

    }

    public static String[] parseExtensions(String extensions) {
        StringTokenizer st = new StringTokenizer(extensions, ",");
        String[] ext = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            ext[i++] = st.nextToken();
        }
        return ext;
    }

    /**
     * Gets the extension of a file.
     *
     * @param fileName the file name
     * @return the extension of the file.
     */
    public static String getExtension(String fileName) {
        String ext = null;
        int i = fileName.lastIndexOf('.');

        if (i > 0 && i < fileName.length() - 1) {
            ext = fileName.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static class FileExtensionFilter extends FileFilter implements FilenameFilter {
        String[] extensions;
        String description;

        public FileExtensionFilter(String extensions, String description) {
            this.extensions = parseExtensions(extensions);
            this.description = description;
        }

        public boolean accept(File f) {
            return f.isDirectory() || accept(null, f.getName());
        }

        public boolean accept(File f, String s) {
            String extension = FileUtils.getExtension(s);
            if (extension != null) {
                for (String extension1 : extensions) {
                    if (extension1.equals("*") || extension1.equalsIgnoreCase(extension)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String getDescription() {
            return description;
        }
    }
}
