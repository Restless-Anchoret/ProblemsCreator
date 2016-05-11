package com.ran.testing.language;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class VisualCppLanguageToolkit implements LanguageToolkit {

    private static final String COMPILING_BAT_FILE_NAME = "compiling.bat";
    private static final String PROPERTIES_FILE_NAME = "visual_cpp.properties";
    private static final String CL_PATH_PROPERTY = "cl_path";
    private static final String VARS_BAT_PATH_PROPERTY = "vars_bat_path";
    
    private static Properties visualCppProperties = null;
    private static Path visualCppPropertiesLocation = null;
    
    private static Properties getVisualCppProperties(Path configFolder) throws FailException {
        if (visualCppProperties == null || !Objects.equals(configFolder, visualCppPropertiesLocation)) {
            visualCppProperties = new Properties();
            try {
                visualCppProperties.load(Files.newInputStream(configFolder.resolve(PROPERTIES_FILE_NAME)));
            } catch (IOException exception) {
                throw new FailException("Fail because of exception while loading " + PROPERTIES_FILE_NAME, exception);
            }
            visualCppPropertiesLocation = configFolder;
        }
        return visualCppProperties;
    }
    
    @Override
    public int compile(Path sourceFile, Path compileFolder, Path configFolder)
            throws FailException {
        Properties visualCppProperties = getVisualCppProperties(configFolder);
        String clPathProperty = visualCppProperties.getProperty(CL_PATH_PROPERTY);
        String varsBatPathProperty = visualCppProperties.getProperty(VARS_BAT_PATH_PROPERTY);
        if (clPathProperty == null || varsBatPathProperty == null) {
            throw new FailException("Compilation failed because properties were not found");
        }
        Path clPath = Paths.get(clPathProperty);
        Path varsBatPath = Paths.get(varsBatPathProperty);
        Path compilingBatPath = compileFolder.resolve(COMPILING_BAT_FILE_NAME);
        String sourceFileName = getFileName(sourceFile);
        Path exePath = compileFolder.resolve(sourceFileName + ".exe");
        Path objPath = compileFolder.resolve(sourceFileName + ".obj");
        if (Files.notExists(clPath) || Files.notExists(varsBatPath) ||
                Files.notExists(sourceFile) || Files.notExists(compileFolder)) {
            throw new FailException("Compilation failed because files were not found");
        }
        try {
            Files.copy(varsBatPath, compilingBatPath, StandardCopyOption.REPLACE_EXISTING);
            try (PrintWriter writer = new PrintWriter(Files.newOutputStream(compilingBatPath, StandardOpenOption.APPEND))) {
                writer.println(quotes(clPath) + " " + quotes(sourceFile) + " " + quotes("/Fe", exePath) +
                        " " + quotes("/Fo", objPath));
                writer.flush();
            }
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(compilingBatPath.toString());
            Process process = null;
            try {
                process = builder.start();
                process.waitFor();
            } finally {
                killProcess(process);
            }
            Files.deleteIfExists(compilingBatPath);
            Files.deleteIfExists(objPath);
            return process.exitValue();
        } catch (IOException | InterruptedException exception) {
            throw new FailException("InterruptedException or IOException while compilation", exception);
        }
    }

    @Override
    public ExecutionInfo execute(Path compileFile, Path inputFile, Path outputFile,
            Path configFolder, int timeLimit, short memoryLimit)
            throws FailException, TimeLimitException,
            MemoryLimitException, SecurityViolatedException {
        if (Files.notExists(compileFile) || Files.notExists(inputFile) ||
                Files.notExists(outputFile)) {
            throw new FailException("Execution failed because files were not found.");
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(compileFile.toString());
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
    
    private String getFileName(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return fileName;
    }
    
    private String quotes(Path path) {
        return quotes("", path);
    }
    
    private String quotes(String prefix, Path path) {
        return "\"" + prefix + path.toString() + "\"";
    }
    
    private void killProcess(Process process) throws InterruptedException {
        if (process != null) {
            process.destroyForcibly();
            process.waitFor();
        }
    }

}