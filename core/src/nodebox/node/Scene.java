package nodebox.node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class Scene {

    public static final String PROCESSING_RENDERER = "processing.renderer";
    public static final String PROCESSING_FRAME_RATE = "processing.frameRate";
    public static final String PROCESSING_DRAW_BACKGROUND = "processing.drawBackground";
    public static final String PROCESSING_BACKGROUND_COLOR = "processing.backgroundColor";
    public static final String PROCESSING_SMOOTH = "processing.smooth";
    public static final String PROCESSING_WIDTH = "processing.width";
    public static final String PROCESSING_HEIGHT = "processing.height";

    public final static Properties DEFAULT_PROPERTIES;

    static {
        DEFAULT_PROPERTIES = new Properties();
        DEFAULT_PROPERTIES.setProperty(PROCESSING_WIDTH, "500");
        DEFAULT_PROPERTIES.setProperty(PROCESSING_HEIGHT, "500");
        DEFAULT_PROPERTIES.setProperty(PROCESSING_RENDERER, "processing.core.PGraphicsJava2D");
        DEFAULT_PROPERTIES.setProperty(PROCESSING_FRAME_RATE, "60");
        DEFAULT_PROPERTIES.setProperty(PROCESSING_DRAW_BACKGROUND, "true");
        DEFAULT_PROPERTIES.setProperty(PROCESSING_BACKGROUND_COLOR, "200,200,200");
        DEFAULT_PROPERTIES.setProperty(PROCESSING_SMOOTH, "true");
    }

    private Network rootNetwork;
    private Properties properties = new Properties(DEFAULT_PROPERTIES);

    public Scene() {
        rootNetwork = new Network();
        rootNetwork.setName("root");
        rootNetwork.setDisplayName("Root");
    }

    private Scene(Network root) {
        rootNetwork = root;
    }

    public Network getRootNetwork() {
        return rootNetwork;
    }

    public void execute(Context context, float time) {
        rootNetwork.execute(context, time);
    }

    //// Properties ////

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    //// Loading ////

    /**
     * Load a scene from the given file.
     * <p/>
     * The manager is used only to look up node classes.
     *
     * @param f       the file to load
     * @param manager the manager used to look up node classes.
     * @return a new node scene
     * @throws RuntimeException When the file could not be found, or parsing failed.
     */
    public static Scene load(File f, NodeManager manager) throws RuntimeException {
        try {
            // The library name is the file name without the ".ndbx" extension.
            // Chop off the .ndbx
            return load(new FileInputStream(f), manager);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error in the XML parser configuration", e);
        } catch (SAXException e) {
            throw new RuntimeException("Error while parsing: " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found " + f, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O error while parsing " + f, e);
        }
    }

    public static Scene load(String xml, NodeManager manager) throws RuntimeException {
        try {
            return load(new ByteArrayInputStream(xml.getBytes("UTF8")), manager);
        } catch (IOException e) {
            throw new RuntimeException("I/O error while parsing.", e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error in the XML parser configuration", e);
        } catch (SAXException e) {
            throw new RuntimeException("Error while parsing: " + e.getMessage(), e);
        }
    }

    public static Scene load(InputStream is, NodeManager manager) throws IOException, ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        NDBXHandler handler = new NDBXHandler(manager);
        parser.parse(is, handler);
        return handler.scene;
    }

    private static class NDBXHandler extends DefaultHandler {

        enum ParseState {
            INVALID, IN_PORT
        }

        private NodeManager manager;
        private Scene scene;
        private Properties sceneProperties = new Properties();
        private Node currentNode;
        private Port currentPort;
        private ParseState state = ParseState.INVALID;
        private StringBuffer characterData;

        public NDBXHandler(NodeManager manager) {
            this.manager = manager;
            resetState();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("ndbx")) {
                startNdbxTag(attributes);
            } else if (qName.equals("property")) {
                startPropertyTag(attributes);
            } else if (qName.equals("node")) {
                startNodeTag(attributes);
            } else if (qName.equals("port")) {
                startPortTag(attributes);
            } else if (qName.equals("conn")) {
                startConnectionTag(attributes);
            } else {
                throw new SAXException("Unknown tag " + qName);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("ndbx")) {
                // Top level element -- parsing finished.
                endNdbxTag();
            } else if (qName.equals("property")) {
                // Do nothing after property tag
            } else if (qName.equals("node")) {
                // Traverse up to the parent.
                // This can result in currentNode being null if we traversed all the way up
                currentNode = currentNode.getNetwork();
            } else if (qName.equals("port")) {
                setPortValue(characterData.toString());
                currentPort = null;
                resetState();
            } else if (qName.equals("conn")) {
                // Do nothing after conn tag
            } else {
                // This should never happen, since the SAX parser has already formally validated the document.
                // Unknown tags should be caught in startElement.
                throw new AssertionError("Unknown end tag " + qName);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            switch (state) {
                case IN_PORT:
                    if (currentPort == null)
                        throw new SAXException("Port value encountered, but no current port.");
                    break;
                default:
                    // Bail out when we don't recognize this state.
                    return;
            }
            // We have a valid character state, so we can safely append to characterData.
            characterData.append(ch, start, length);
        }

        /**
         * Called after valid character data was processed.
         * <p/>
         * This makes sure no extraneous data is added.
         */
        private void resetState() {
            state = ParseState.INVALID;
            characterData = new StringBuffer();
        }

        private void startNdbxTag(Attributes attributes) throws SAXException {
            // Make sure we use the correct format.
            String formatVersion = attributes.getValue("formatVersion");
            if (formatVersion == null)
                throw new SAXException("NodeBox file does not have required attribute formatVersion.");
            if (!formatVersion.equals("3"))
                throw new SAXException("Unknown formatVersion " + formatVersion);
        }

        private void endNdbxTag() throws SAXException {
            if (scene == null) {
                throw new SAXException("Empty ndbx file.");
            }
            scene.properties.putAll(sceneProperties);
        }

        private void startPropertyTag(Attributes attributes) throws SAXException {
            if (currentNode != null) {
                throw new SAXException("Property tags should appear right below the ndbx tag.");
            }
            String key = parseString(attributes, "key", "Key attribute is required in property tags.");
            String value = parseString(attributes, "value", "Value attribute is required in property tags.");
            sceneProperties.setProperty(key, value);
        }

        private void startNodeTag(Attributes attributes) throws SAXException {
            // Since we're going to be adding a child node, check that the current node is a network.
            // The exception is if the currentNode is null, which means we're creating the root node.
            if (currentNode != null && !(currentNode instanceof Network)) {
                throw new SAXException("Trying to add child node to " + currentNode + ", which is not a network.");
            }
            String name = parseString(attributes, "name", "Name attribute is required in node tags.");
            String type = parseString(attributes, "type", "Type attribute is required in node tags.");
            // Create the child at the root of the node library or the current parent
            Node newNode = manager.createNode(type);
            // Set node attributes.
            newNode.setName(name);
            String displayName = attributes.getValue("displayName");
            if (displayName != null) {
                newNode.setDisplayName(displayName);
            }
            int x = parseInt(attributes, "x");
            int y = parseInt(attributes, "y");
            newNode.setPosition(x, y);
            if (currentNode == null) {
                if (newNode instanceof Network) {
                    // We're creating the root node and scene.
                    scene = new Scene((Network) newNode);
                } else {
                    throw new SAXException("The root node is not a Network.");
                }
            } else {
                // Add the node to the network.
                // We checked if the current node is a Network at the top of this method.
                Network currentNetwork;
                currentNetwork = (Network) currentNode;
                currentNetwork.addChild(newNode);
            }

            // If this is rendered node, mark it.
            if ("true".equals(attributes.getValue("rendered"))) {
                newNode.setRenderedNode();
            }

            // Go down into the current node; this will now become the current node.
            currentNode = newNode;
        }

        private void startPortTag(Attributes attributes) throws SAXException {
            state = ParseState.IN_PORT;
            String name = parseString(attributes, "name", "Name attribute is required in port tags.");
            try {
                currentPort = currentNode.getPort(name);
            } catch (IllegalArgumentException e) {
                throw new SAXException("The port " + name + " on node" + currentNode + " could not be found.", e);
            }
        }

        private void startConnectionTag(Attributes attributes) throws SAXException {
            // Since we're going to be adding a child connection, check that the current node is a network.
            if (!(currentNode instanceof Network)) {
                throw new SAXException("Trying to make a connection node in " + currentNode + ", which is not a network.");
            }
            String outputString = parseString(attributes, "output", "Output attribute is required in connection tags.");
            String outputPortString = parseString(attributes, "outputPort", "Output port attribute is required in connection tags.");
            String inputString = parseString(attributes, "input", "Input attribute is required in connection tags.");
            String inputPortString = parseString(attributes, "inputPort", "Input port attribute is required in connection tags.");
            Network network = (Network) currentNode;

            Node outputNode = network.getChild(outputString);
            Node inputNode = network.getChild(inputString);
            Port output = outputNode.getPort(outputPortString);
            Port input = inputNode.getPort(inputPortString);

            network.connect(output, input);
        }

        /**
         * Sets the value on the current port.
         *
         * @param stringValue the value of the parameter, to be parsed.
         * @throws org.xml.sax.SAXException when there is no current port or if the value could not be parsed.
         */
        private void setPortValue(String stringValue) throws SAXException {
            if (currentPort == null) throw new SAXException("There is no current port.");
            if (currentPort.isPersistable()) {
                PersistablePort persistablePort = (PersistablePort) currentPort;
                Object value = persistablePort.parseValue(stringValue);
                currentPort.setValue(value);
            }
        }

        /**
         * Get a string from the attributes. Throw an error if the requested attribute could not be found.
         *
         * @param attributes    the list of attributes
         * @param attributeName the requested attribute
         * @param errorMessage  the error to throw if the attribute could not be found
         * @return the attribute value
         * @throws SAXException if the attribute could not be found
         */
        private String parseString(Attributes attributes, String attributeName, String errorMessage) throws SAXException {
            String s = attributes.getValue(attributeName);
            if (s != null) {
                return s;
            } else {
                throw new SAXException(errorMessage);
            }
        }

        /**
         * Get an integer from the attributes. Return a default value of zero if the requested attribute could not be found.
         *
         * @param attributes    the list of attributes
         * @param attributeName the requested attribute
         * @return the attribute value or 0 if the attribute could not be found
         */
        private int parseInt(Attributes attributes, String attributeName) {
            String s = attributes.getValue(attributeName);
            if (s == null) return 0;
            return Integer.parseInt(s);
        }

    }

    //// Saving ////

    public void save(File f) {
        StreamResult streamResult = new StreamResult(f);
        write(this, streamResult);
    }

    public String toXML() {
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        write(this, streamResult);
        return writer.toString();
    }

    private static void write(Scene scene, StreamResult streamResult) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            // Build the header.
            Element rootElement = doc.createElement("ndbx");
            doc.appendChild(rootElement);
            rootElement.setAttribute("formatVersion", "3");

            // Write out the scene properties.
            for (Object key : scene.properties.keySet()) {
                if (key instanceof String) {
                    Object value = scene.properties.get(key);
                    if (value instanceof String) {
                        Element propertyElement = doc.createElement("property");
                        rootElement.appendChild(propertyElement);
                        propertyElement.setAttribute("key", (String) key);
                        propertyElement.setAttribute("value", (String) value);
                    }
                }
            }

            // Write out the root node.
            writeNode(doc, rootElement, scene.getRootNetwork());

            // Convert the document to XML.
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.transform(domSource, streamResult);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeNode(Document doc, Element parent, Node node) {
        Element el = doc.createElement("node");
        parent.appendChild(el);
        el.setAttribute("name", node.getName());
        el.setAttribute("type", NodeManager.nodeId(node.getClass()));
        if (!node.hasDefaultDisplayName()) {
            el.setAttribute("displayName", node.getDisplayName());
        }
        Point position = node.getPosition();
        el.setAttribute("x", Integer.toString(position.x));
        el.setAttribute("y", Integer.toString(position.y));
        if (node.isRenderedNode()) {
            el.setAttribute("rendered", "true");
        }

        // Add the input ports
        for (Port port : node.getInputPorts()) {
            writePort(doc, el, port);
        }

        if (node instanceof Network) {
            Network network = (Network) node;

            // Add all child nodes
            List<Node> children = network.getChildren();
            Collections.sort(children, new NodeNameComparator());
            for (Node child : children) {
                writeNode(doc, el, child);
            }

            // Add all child connections
            for (Connection conn : network.getConnections()) {
                writeConnection(doc, el, conn);
            }
        }
    }

    private static void writePort(Document doc, Element parent, Port port) {
        if (port.isPersistable()) {
            PersistablePort persistablePort = (PersistablePort) port;
            Element el = doc.createElement("port");
            parent.appendChild(el);
            el.setAttribute("name", port.getName());
            el.appendChild(doc.createTextNode(persistablePort.getValueAsString()));
        }
    }

    private static void writeConnection(Document doc, Element parent, Connection connection) {
        Element el = doc.createElement("conn");
        parent.appendChild(el);
        el.setAttribute("output", connection.getOutputNode().getName());
        el.setAttribute("outputPort", connection.getOutputPort().getName());
        el.setAttribute("input", connection.getInputNode().getName());
        el.setAttribute("inputPort", connection.getInputPort().getName());
    }

    private static class NodeNameComparator implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            return node1.getName().compareTo(node2.getName());
        }
    }

}
