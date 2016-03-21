package com.ran.filesystem.descriptor;

import com.ran.filesystem.logging.FileSystemLogging;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class EntityDescriptor {

    private Map<String, String> properties = new LinkedHashMap<>();
    private Path path;
    private String rootNodeName;

    public EntityDescriptor(Path path, String rootNodeName) {
        this.path = path;
        this.rootNodeName = rootNodeName;
    }
    
    public String getProperty(String propertyName) {
        return properties.get(propertyName);
    }
    
    public void setProperty(String propertyName, String propertyValue) {
        properties.put(propertyName, propertyValue);
    }
    
    public void load() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(path.toFile());
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    String propertyName = node.getNodeName();
                    String propertyValue = "";
                    if (node.getFirstChild() != null) {
                        propertyValue = node.getFirstChild().getNodeValue();
                    }
                    properties.put(propertyName, propertyValue);
                }
            }
        } catch (Exception exception) {
            FileSystemLogging.logger.log(Level.FINE, "Exception while loading submission descriptor", exception);
        }
    }
    
    public void persist() {
        try (OutputStream stream = Files.newOutputStream(path)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Node submissionElement = document.createElement(rootNodeName);
            for (Map.Entry<String, String> entry: properties.entrySet()) {
                Node newNode = document.createElement(entry.getKey());
                newNode.appendChild(document.createTextNode(entry.getValue()));
                submissionElement.appendChild(newNode);
            }
            document.appendChild(submissionElement);
            DOMImplementation implementation = document.getImplementation();
            DOMImplementationLS implementationLS = (DOMImplementationLS)implementation.getFeature("LS", "3.0");
            LSSerializer serializer = implementationLS.createLSSerializer();
            serializer.getDomConfig().setParameter("format-pretty-print", true);
            LSOutput output = implementationLS.createLSOutput();
            output.setEncoding("UTF-8");
            output.setByteStream(stream);
            serializer.write(document, output);
        } catch (Exception exception) {
            FileSystemLogging.logger.log(Level.FINE, "Exception while loading submission descriptor", exception);
        }
    }
    
    protected Integer parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
    
    protected String intToString(Integer number) {
        if (number == null) {
            return "";
        } else {
            return number.toString();
        }
    }
    
}