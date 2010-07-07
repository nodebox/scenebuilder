package scenebuilder.library.code;

import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import scenebuilder.model.Context;
import scenebuilder.model.RenderingNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JavaCode extends RenderingNode implements ActionListener {
    private JTextArea codeArea;
    private static final File TEMPORARY_DIRECTORY = new File("_tmp");
    private Method runMethod;

    public JavaCode() {
        setAttribute(DISPLAY_NAME_ATTRIBUTE, "Java Code");
        setAttribute(DESCRIPTION_ATTRIBUTE, "Runs Java code.");
    }

    @Override
    public JComponent createEditorComponent() {
        JPanel contents = new JPanel(new BorderLayout());
        codeArea = new JTextArea();
        contents.add(codeArea, BorderLayout.CENTER);
        JButton runButton = new JButton("Run");
        runButton.addActionListener(this);
        contents.add(runButton, BorderLayout.NORTH);
        return contents;
    }

    /**
     * The user pushed the run button.
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        runMethod = null;
        String code = codeArea.getText();
        String wrappedCode = wrapCode(code);
        File temporaryFile = writeTemporaryFile(wrappedCode);
        try {
            compileCode(temporaryFile);
            runCode();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            deleteTemporaryFile(temporaryFile);
        }
    }

    /**
     * Java requires code to be in a class. Wrap the code in a static method of a class.
     * <p/>
     * This method does not check if the code is already in a class.
     *
     * @param code the original, unwrapped code.
     * @return new code wrapped in a proper class.
     */
    private String wrapCode(String code) {
        StringBuffer sb = new StringBuffer(code.length() + 100);
        sb.append("public class CodeObject {\n");
        sb.append("public static void run() {\n");
        sb.append(code);
        sb.append("}\n"); // Close off run method
        sb.append("}\n"); // Close off class
        return sb.toString();
    }

    private File writeTemporaryFile(String code) {
        TEMPORARY_DIRECTORY.mkdirs();
        File temporaryFile = new File(TEMPORARY_DIRECTORY, "CodeObject.java");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            temporaryFile.delete();
            throw new RuntimeException("COMPILE: Could not write temporary file " + temporaryFile);
        }
        return temporaryFile;
    }

    private void compileCode(File file) {
        PrintWriter outWriter = new PrintWriter(System.out);
        PrintWriter errWriter = new PrintWriter(System.out);
        String[] commandLine = new String[]{
                "-d", TEMPORARY_DIRECTORY.getAbsolutePath(),
                file.getAbsolutePath()
        };
        BatchCompiler.compile(commandLine, outWriter, errWriter, null);
        outWriter.flush();
        errWriter.flush();
    }

    private void runCode() throws Exception {
        URLClassLoader loader;
        URL temporaryDirectoryURL = TEMPORARY_DIRECTORY.toURI().toURL();
        URL[] urls = new URL[]{temporaryDirectoryURL};
        loader = new URLClassLoader(urls);
        Class codeObjectClass;
        codeObjectClass = loader.loadClass("CodeObject");
        runMethod = codeObjectClass.getMethod("run");
        runMethod.invoke(null);
    }

    private void deleteTemporaryFile(File file) {
        file.delete();
    }

    @Override
    public boolean execute(Context context, double time) {
        if (runMethod == null) return true;
        try {
            runMethod.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
