package com.ran.development.temp;

import com.ran.development.gen.Generator;
import com.ran.development.util.DevelopmentResult;
import com.ran.development.util.Utils;
import com.ran.development.gen.MultiGenerator;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import com.ran.development.util.DevelopmentListener;
import com.ran.development.valid.MultiValidator;
import com.ran.development.valid.Validator;

public class DevelopmentTest {

    public static void main(String[] args) {
        //int testsQuantity = 5;
        int testsQuantity = 1;
        String[] arguments = { "200000", "200000", "10000" };
        System.out.println("Generating:");
        testGenerators(testsQuantity, arguments);
        //System.out.print("\n\n\n");
        //System.out.println("Validating:");
        //testValidators(testsQuantity, arguments);
    }
    
    private static void testGenerators(int testsQuantity, String[] arguments) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilingResult = compiler.run(null, null, null, "file_system\\NewGenerator.java");
        System.out.println("Compiling result: " + compilingResult);
        MultiGenerator multiGenerator = new MultiGenerator();
        Path[] paths = new Path[testsQuantity];
        for (int i = 0; i < testsQuantity; i++) {
            paths[i] = Paths.get("file_system", "input" + (i + 1) + ".txt");
        }
        multiGenerator.setPaths(paths);
        multiGenerator.setArguments(arguments);
        //multiGenerator.setArguments(new String[] { "11", "100", "1000" });
        multiGenerator.setRandomSeed(100);
        multiGenerator.setGeneratorSupplier(Utils.getSupplier(Generator.class, Paths.get("file_system"), "NewGenerator"));
        multiGenerator.addDevelopmentListener(getListener());
        multiGenerator.performGenerating();
    }
    
    private static void testValidators(int testsQuantity, String[] arguments) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilingResult = compiler.run(null, null, null, "file_system\\NewValidator.java");
        System.out.println("Compiling result: " + compilingResult);
        MultiValidator multiValidator = new MultiValidator();
        Path[] paths = new Path[testsQuantity];
        for (int i = 0; i < testsQuantity; i++) {
            paths[i] = Paths.get("file_system", "input" + (i + 1) + ".txt");
        }
        multiValidator.setPaths(paths);
        multiValidator.setArguments(arguments);
        multiValidator.setGeneratorSupplier(Utils.getSupplier(Validator.class, Paths.get("file_system"), "NewValidator"));
        multiValidator.addDevelopmentListener(getListener());
        multiValidator.performValidating();
    }
    
    private static DevelopmentListener getListener() {
        return new DevelopmentListener() {
            @Override
            public void processingStarted() {
                System.out.println("Processing started.");
            }
            @Override
            public void taskProcessingStarted(int taskNumber) {
                System.out.println("Task #" + taskNumber + " processing started.");
            }
            @Override
            public void taskIsProcessing(int taskNumber, long timeFromStart) {
                System.out.println("Task #" + taskNumber + " is processing ..... " + timeFromStart + " ms.");
            }
            @Override
            public void taskIsDone(DevelopmentResult result) {
                System.out.println("Task #" + result.getTaskNumber() +
                        " is done in " + result.getTime() + " ms.");
                System.out.println("Result info: " + result.getInfo());
                System.out.println("Message: " + result.getMessage() + "\n");
            }
            @Override
            public void processingFinished() {
                System.out.println("Processing finished");
            }
        };
    }
    
}