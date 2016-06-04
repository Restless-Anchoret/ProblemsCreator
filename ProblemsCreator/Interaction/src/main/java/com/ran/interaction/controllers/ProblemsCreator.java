package com.ran.interaction.controllers;

import com.ran.filesystem.supplier.FileSupplier;
import com.ran.filesystem.supplier.StandardFileSupplier;
import com.ran.interaction.logging.InteractionLogging;
import com.ran.interaction.support.TestingFileSupplierImpl;
import com.ran.testing.system.MultithreadTestingSystem;
import com.ran.testing.system.TestingSystem;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProblemsCreator {

    private static final String TESTING_SYSTEM_THREADS_DEFAULT = "10";
    private static final String FILE_SYSTEM_PATH_DEFAULT = System.getProperty("user.dir");

    private TestingSystem testingSystem;
    private FileSupplier fileSupplier;

    public void init() {
        Map<String, String> creatorParams = new CreatorParamsReader().getCreatorParams();
        String fileSystemPath = creatorParams.getOrDefault(
                CreatorParamsReader.FILE_SYSTEM_PATH_PARAM_NAME, FILE_SYSTEM_PATH_DEFAULT);
        fileSupplier = new StandardFileSupplier(Paths.get(fileSystemPath));
        String testingSystemThreads = creatorParams.getOrDefault(CreatorParamsReader
                .TESTING_SYSTEM_THREADS_PARAM_NAME, TESTING_SYSTEM_THREADS_DEFAULT);
        MultithreadTestingSystem multithreadTestingSystem
                = MultithreadTestingSystem.getDefault();
        multithreadTestingSystem.setThreadsQuantity(Integer.parseInt(testingSystemThreads));
        multithreadTestingSystem.setFileSupplier(new TestingFileSupplierImpl(fileSupplier));
        testingSystem = multithreadTestingSystem;
        testingSystem.start();
    }

    public void stop() {
        testingSystem.stop();
    }

    public TestingSystem getTestingSystem() {
        return testingSystem;
    }

    public FileSupplier getFileSupplier() {
        return fileSupplier;
    }

}



class CreatorParamsReader {

    public static final String CREATOR_PARAMS_FILE = "creator-params.xml";
    public static final String TESTING_SYSTEM_THREADS_PARAM_NAME = "testingSystemThreads";
    public static final String FILE_SYSTEM_PATH_PARAM_NAME = "fileSystemPath";
    public static final String PARAM_NAME_NODE_NAME = "name";
    public static final String PARAM_VALUE_NODE_NAME = "value";

    public Map<String, String> getCreatorParams() {
        Map<String, String> creatorParams = new HashMap<>();
        try (InputStream creatorParamStream = ProblemsCreator.class
                .getResourceAsStream(CREATOR_PARAMS_FILE)) {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document document = documentBuilder.parse(creatorParamStream);
            Element rootElement = document.getDocumentElement();
            NodeList paramList = rootElement.getChildNodes();
            for (int i = 0; i < paramList.getLength(); i++) {
                Node node = paramList.item(i);
                if (node instanceof Element) {
                    updateCreatorParamsMap(creatorParams, node);
                }
            }
        } catch (Exception exception) {
            InteractionLogging.logger.log(Level.FINE,
                    "Exception while reading creator-params.xml", exception);
        }
        return creatorParams;
    }

    private void updateCreatorParamsMap(Map<String, String> creatorParams, Node paramNode) {
        NodeList nodeList = paramNode.getChildNodes();
        String paramName = null;
        String paramValue = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                String nodeName = node.getNodeName();
                String nodeValue = node.getFirstChild().getNodeValue();
                switch (nodeName) {
                    case PARAM_NAME_NODE_NAME: paramName = nodeValue; break;
                    case PARAM_VALUE_NODE_NAME: paramValue = nodeValue; break;
                }
            }
        }
        creatorParams.put(paramName, paramValue);
    }
}
