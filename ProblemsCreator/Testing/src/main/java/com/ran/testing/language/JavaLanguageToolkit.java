package com.ran.testing.language;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JavaLanguageToolkit implements LanguageToolkit {
    
    private static final String POLICY_FILE_NAME = "java_problem.policy";
    private static final String PROPERTIES_FILE_NAME = "java.properties";
    private static final String JAVA_PATH_PROPERTY = "java_path";
    private static final String STACK_SIZE_PROPERTY = "stack_size";
    
    private static final String STACK_OVERFLOW_SIGN = "StackOverflowError";
    private static final String HEAP_OVERFLOW_SIGN = "OutOfMemoryError";
    private static final String SECURITY_VIOLATED_SIGN = "SecurityManager";
    
    private static Properties javaProperties = null;
    private static Path javaPropertiesLocation = null;
    
    private static Properties getJavaProperties(Path configFolder) throws FailException {
        if (javaProperties == null || !Objects.equals(configFolder, javaPropertiesLocation)) {
            javaProperties = new Properties();
            try {
                javaProperties.load(Files.newInputStream(configFolder.resolve(PROPERTIES_FILE_NAME)));
            } catch (IOException exception) {
                throw new FailException("Fail because of exception while loading " + PROPERTIES_FILE_NAME, exception);
            }
            javaPropertiesLocation = configFolder;
        }
        return javaProperties;
    }
    
    @Override
    public int compile(Path sourceFile, Path compileFolder, Path configFolder) throws FailException {
        return compile(sourceFile, compileFolder, configFolder, EMPTY_OUTPUT_STREAM);
    }

    @Override
    public int compile(Path sourceFile, Path compileFolder, Path configFolder, OutputStream errorStream) throws FailException {
        if (Files.notExists(sourceFile) || Files.notExists(compileFolder)) {
            throw new FailException("Compilation failed because files were not found.");
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        return compiler.run(null, null, errorStream, "-d", compileFolder.toString(), sourceFile.toString());
    }

    @Override
    public ExecutionInfo execute(Path compileFile, Path inputFile, Path outputFile,
            Path configFolder, int timeLimit, short memoryLimit)
            throws FailException, TimeLimitException, MemoryLimitException, SecurityViolatedException {
        Properties javaProperties = getJavaProperties(configFolder);
        String javaPathProperty = javaProperties.getProperty(JAVA_PATH_PROPERTY);
        String stackSizeProperty = javaProperties.getProperty(STACK_SIZE_PROPERTY);
        Path policyPath = configFolder.resolve(POLICY_FILE_NAME);
        if (Files.notExists(compileFile) || Files.notExists(inputFile) ||
                Files.notExists(outputFile) || Files.notExists(policyPath) ||
                javaPathProperty == null || stackSizeProperty == null) {
            throw new FailException("Execution failed because files or properties were not found.");
        }
        try {
            Path javaPath = Paths.get(javaPathProperty);
            String memoryOption = "-Xmx" + memoryLimit + "M";
            String stackOption = "-Xss" + stackSizeProperty + "M";
            String managerOption = "-Djava.security.manager";
            String policyOption = "-Djava.security.policy==" + policyPath.toString();
            String className = getClassName(compileFile);
            ProcessBuilder processBuilder = new ProcessBuilder("\"" + javaPath.toString() + "\"",
                    memoryOption, stackOption, managerOption, policyOption, className);
            Path compileFolder = compileFile.getParent();
            processBuilder.directory(compileFolder.toFile());
            processBuilder.redirectInput(inputFile.toFile());
            processBuilder.redirectOutput(outputFile.toFile());
            Process process = null;
            try {
                long start = System.currentTimeMillis();
                process = processBuilder.start();
                process.waitFor(timeLimit, TimeUnit.MILLISECONDS);
                int decisionTime = (int)(System.currentTimeMillis() - start);
                if (process.isAlive()) {
                    throw new TimeLimitException(decisionTime);
                }
                int exitValue = process.exitValue();
                if (exitValue != 0) {
                    analyseErrorStream(process.getErrorStream());
                }
                return new ExecutionInfo(exitValue, decisionTime, null);
            } finally {
                if (process != null) {
                    process.destroyForcibly();
                    process.waitFor();
                }
            }
        } catch (InterruptedException | IOException exception) {
            throw new FailException("InterruptedException or IOException while execution.", exception);
        }
    }
    
    private String getClassName(Path classFile) {
        String fileName = classFile.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return fileName;
    }
    
    private void analyseErrorStream(InputStream errorStream) throws MemoryLimitException, SecurityViolatedException {
        Scanner scanner = new Scanner(errorStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(STACK_OVERFLOW_SIGN) || line.contains(HEAP_OVERFLOW_SIGN)) {
                throw new MemoryLimitException();
            } else if (line.contains(SECURITY_VIOLATED_SIGN)) {
                throw new SecurityViolatedException();
            }
        }
    }

}