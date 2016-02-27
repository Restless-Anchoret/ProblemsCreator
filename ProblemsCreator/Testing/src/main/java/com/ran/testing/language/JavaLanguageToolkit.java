package com.ran.testing.language;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JavaLanguageToolkit implements LanguageToolkit {
    
    private static final String STACK_OVERFLOW_SIGN = "StackOverflowError";
    private static final String HEAP_OVERFLOW_SIGN = "OutOfMemoryError";
    private static final String SECURITY_VIOLATED_SIGN = "SecurityManager";
    
    private static final OutputStream EMPTY_ERROR_STREAM = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }
    };
    
    @Override
    public int compile(Path sourceFile, Path compileFolder, Path configFolder) throws FailException {
        if (Files.notExists(sourceFile) || Files.notExists(compileFolder)) {
            throw new FailException("Compilation failed because files not found.");
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        return compiler.run(null, null, EMPTY_ERROR_STREAM, "-d", compileFolder.toString(), sourceFile.toString());
    }

    @Override
    public ExecutionInfo execute(Path compileFile, Path inputFile, Path outputFile,
            Path configFolder, int timeLimit, short memoryLimit)
            throws FailException, TimeLimitException, MemoryLimitException, SecurityViolatedException {
        Path policyPath = Paths.get(configFolder.toString(), "java_problem.policy");
        if (Files.notExists(compileFile) || Files.notExists(inputFile) ||
                Files.notExists(outputFile) || Files.notExists(policyPath)) {
            throw new FailException("Execution failed because files not found.");
        }
        try {
            Path compileFolder = compileFile.getParent();
            String className = getClassName(compileFile);
            String memoryOption = "-Xmx" + memoryLimit + "M";
            String policyOption = "-Djava.security.policy==" + policyPath.toString();
            ProcessBuilder processBuilder = new ProcessBuilder("java", memoryOption, "-Xss8M",
                    "-Djava.security.manager", policyOption, className);
            processBuilder.directory(compileFolder.toFile());
            processBuilder.redirectInput(inputFile.toFile());
            processBuilder.redirectOutput(outputFile.toFile());
            Process process = null;
            try {
                long start = System.currentTimeMillis();
                process = processBuilder.start();
                process.waitFor(timeLimit, TimeUnit.MILLISECONDS);
                if (process.isAlive()) {
                    throw new TimeLimitException();
                }
                int decisionTime = (int)(System.currentTimeMillis() - start);
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