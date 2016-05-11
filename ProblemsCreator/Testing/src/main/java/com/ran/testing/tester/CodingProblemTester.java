package com.ran.testing.tester;

import com.ran.testing.evaluation.EvaluationSystem;
import com.ran.testing.language.FailException;
import com.ran.testing.language.LanguageToolkit;
import com.ran.testing.language.MemoryLimitException;
import com.ran.testing.language.SecurityViolatedException;
import com.ran.testing.language.TimeLimitException;
import com.ran.testing.logging.TestingLogging;
import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.TestingFileSupplier;
import com.ran.testing.system.TestingInfo;
import com.ran.testing.system.Verdict;
import com.ran.testing.system.VerdictInfo;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class CodingProblemTester implements ProblemTester {

    @Override
    public void performTesting(TestingFileSupplier fileSupplier, TestingInfo info) {
        Path sourceFile = info.getCodeFileSupplier().getSourceFile();
        Path compileFolder = info.getCodeFileSupplier().getCompileFolder();
        Path configFolder = fileSupplier.getConfigurationFolder();
        int compilationResult = 0;
        try {
            compilationResult = info.getLanguageToolkit().compile(sourceFile, compileFolder, configFolder);
        } catch (FailException exception) {
            TestingLogging.logger.log(Level.FINE, "FailException while compilation of decision", exception);
            if (exception.getCause() != null) {
                TestingLogging.logger.log(Level.FINE, "FailException while compilation of decision (cause)", exception.getCause());
            }
            info.setVerdictInfo(VerdictInfo.VERDICT_FAIL);
            return;
        }
        if (compilationResult != 0) {
            info.setVerdictInfo(VerdictInfo.VERDICT_COMPILE_ERROR);
            return;
        }
        EvaluationSystem evaluationSystem = info.getEvaluationSystem();
        evaluationSystem.orderTesting(new CodingTesterDelegate(info, fileSupplier), info.isPretestsOnly());
        VerdictInfo verdictInfo = evaluationSystem.getVerdictInfo(info.getTestTable(), info.isPretestsOnly());
        info.setVerdictInfo(verdictInfo);
    }
    
    private static class CodingTesterDelegate implements EvaluationSystem.TesterDelegate {

        private TestingInfo info;
        private TestingFileSupplier fileSupplier;
        private Path configFolder;

        public CodingTesterDelegate(TestingInfo testingInfo, TestingFileSupplier fileSupplier) {
            this.info = testingInfo;
            this.fileSupplier = fileSupplier;
            this.configFolder = fileSupplier.getConfigurationFolder();
        }
        
        @Override
        public TestTable getTestTable() {
            return info.getTestTable();
        }

        @Override
        public Integer performTestGroup(TestGroupType type, boolean upToFirstFailure) {
            Integer wrongTestNumber = null;
            int testsQuantity = info.getTestTable().getTestsQuantity(type);
            for (int testNumber = 1; testNumber <= testsQuantity; testNumber++) {
                VerdictInfo verdictInfo = performOneTest(type, testNumber);
                info.getTestTable().putVerdictInfo(type, testNumber, verdictInfo);
                if (wrongTestNumber == null && verdictInfo.getVerdict() != Verdict.ACCEPTED) {
                    wrongTestNumber = testNumber;
                }
                if (upToFirstFailure && verdictInfo.getVerdict() != Verdict.ACCEPTED) {
                    break;
                }
            }
            return wrongTestNumber;
        }

        @Override
        public VerdictInfo performOneTest(TestGroupType type, int testNumber) {
            Path outputFile = fileSupplier.getTempFile();
            try {
                if (outputFile == null || Files.notExists(outputFile)) {
                    TestingLogging.logger.fine("Temp file for output was not found");
                    return VerdictInfo.VERDICT_FAIL;
                }
                Path inputFile = info.getProblemFileSupplier().getTestInputFile(type, testNumber);
                Path answerFile = info.getProblemFileSupplier().getTestAnswerFile(type, testNumber);
                Path decisionFile = info.getCodeFileSupplier().getCompileFile();
                if (Files.notExists(inputFile) || Files.notExists(answerFile) || Files.notExists(decisionFile)) {
                    TestingLogging.logger.fine("Input file or answer file or decision file were not found");
                    return VerdictInfo.VERDICT_FAIL;
                }
                try {
                    LanguageToolkit.ExecutionInfo executionInfo = info.getLanguageToolkit().execute(decisionFile,
                            inputFile, outputFile, configFolder, info.getTimeLimit(), info.getMemoryLimit());
                    if (executionInfo.getExitCode() != 0) {
                        return new VerdictInfo(Verdict.RUNTIME_ERROR)
                                .setDecisionTime(executionInfo.getDecisionTime())
                                .setDecisionMemory(executionInfo.getDecisionMemory());
                    }
                    Verdict verdict = info.getChecker().check(inputFile, outputFile, answerFile);
                    return new VerdictInfo(verdict)
                            .setDecisionTime(executionInfo.getDecisionTime())
                            .setDecisionMemory(executionInfo.getDecisionMemory());
                } catch (TimeLimitException exception) {
                    return new VerdictInfo(Verdict.TIME_LIMIT).setDecisionTime(exception.getDecisionTime());
                } catch (MemoryLimitException exception) {
                    return VerdictInfo.VERDICT_MEMORY_LIMIT;
                } catch (SecurityViolatedException exception) {
                    return VerdictInfo.VERDICT_SECUR_VIOL;
                } catch (FailException exception) {
                    TestingLogging.logger.log(Level.FINE, "FailException while execution of decision", exception);
                    if (exception.getCause() != null) {
                        TestingLogging.logger.log(Level.FINE, "FailException while execution of decision (cause)", exception.getCause());
                    }
                    return VerdictInfo.VERDICT_FAIL;
                }
            } finally {
                fileSupplier.deleteTempFile(outputFile);
            }
        }
        
    }

}