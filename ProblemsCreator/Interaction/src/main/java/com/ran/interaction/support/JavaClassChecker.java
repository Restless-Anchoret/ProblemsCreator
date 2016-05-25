package com.ran.interaction.support;

import com.ran.development.check.WrapperChecker;
import com.ran.development.util.Utils;
import com.ran.testing.checker.Checker;
import com.ran.testing.checker.CheckerRegistry;
import com.ran.testing.logging.TestingLogging;
import com.ran.testing.registry.Suppliers;
import com.ran.testing.system.ProblemFileSupplier;
import com.ran.testing.system.Verdict;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaClassChecker implements Checker {

    private static final String SPECIAL_CHECKER_ID = "special_checker";
    
    public static void plugInClass() {
        CheckerRegistry.registry().put(SPECIAL_CHECKER_ID,
                Suppliers.createNewInstanceSupplier(JavaClassChecker.class));
    }
    
    private WrapperChecker wrapperChecker = null;

    @Override
    public void initialize(ProblemFileSupplier problemFileSupplier) {
        wrapperChecker = new WrapperChecker(
                Utils.getSupplier(com.ran.development.check.Checker.class,
                problemFileSupplier.getCheckerClassFile()));
    }
    
    @Override
    public Verdict check(Path inputPath, Path outputPath, Path answerPath) {
        if (wrapperChecker == null) {
            TestingLogging.logger.fine("WrapperChecker object was not found");
            return Verdict.FAIL;
        }
        if (Files.notExists(inputPath) || Files.notExists(outputPath) || Files.notExists(answerPath)) {
            TestingLogging.logger.fine("Input, output or answer file was not found");
            return Verdict.FAIL;
        }
        int checkingResult = wrapperChecker.check(inputPath, outputPath, answerPath);
        return convertToVerdict(checkingResult);
    }
    
    private Verdict convertToVerdict(int checkingResult) {
        switch (checkingResult) {
            case com.ran.development.check.Checker.OK: return Verdict.ACCEPTED;
            case com.ran.development.check.Checker.WRONG_ANSWER: return Verdict.WRONG_ANSWER;
            default: return Verdict.FAIL;
        }
    }

}