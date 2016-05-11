package com.ran.testing.temp;

import com.ran.testing.checker.Checker;
import com.ran.testing.checker.CheckerRegistry;
import com.ran.testing.evaluation.EvaluationSystem;
import com.ran.testing.evaluation.EvaluationSystemRegistry;
import com.ran.testing.language.LanguageToolkit;
import com.ran.testing.language.LanguageToolkitRegistry;
import com.ran.testing.system.MultithreadTestingSystem;
import com.ran.testing.system.ProblemFileSupplier;
import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestResultHandler;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.TestingInfo;
import com.ran.testing.tester.ProblemTester;
import com.ran.testing.tester.ProblemTesterRegistry;
import java.util.Collection;

public class TestingModuleTest {
    
    public static void main(String[] args) {
//        print(ProblemTesterRegistry.registry().getAvailableIds());
//        print(EvaluationSystemRegistry.registry().getAvailableIds());
//        print(LanguageToolkitRegistry.registry().getAvailableIds());
//        print(CheckerRegistry.registry().getAvailableIds());

        SimpleFileSupplier fileSupplier = new SimpleFileSupplier();
        MultithreadTestingSystem.getDefault().setFileSupplier(fileSupplier);
        MultithreadTestingSystem.getDefault().start();
        System.out.println("Started");
        
        TestResultHandler handler = new ConsoleResultHandler();
        ProblemTester tester = ProblemTesterRegistry.registry().get("coding");
        EvaluationSystem evaluationSystem = EvaluationSystemRegistry.registry().get("icpc");
        LanguageToolkit javaToolkit = LanguageToolkitRegistry.registry().get("java");
        LanguageToolkit cppToolkit = LanguageToolkitRegistry.registry().get("visual_cpp");
        Checker checker = CheckerRegistry.registry().getDefault();
        ProblemFileSupplier problemFileSupplier = new SimpleProblemFileSupplier(fileSupplier, "1");
        
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "1"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "2"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "3"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "4"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "5"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "6"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, javaToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "7"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, cppToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "8"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        MultithreadTestingSystem.getDefault().addSubmission(new TestingInfo(handler, tester, evaluationSystem, cppToolkit, checker,
                new SimpleCodeFileSupplier(fileSupplier, "9"), problemFileSupplier, false, 1000, (short)64, getTestTable()));
        
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            
        }
        
        MultithreadTestingSystem.getDefault().stop();
        System.out.println("Stopped");
    }
    
    private static void print(Collection<String> ids) {
        for (String id: ids) {
            System.out.println(id);
        }
        System.out.println();
    }
    
    private static TestTable getTestTable() {
        TestTable table = new TestTable();
        table.putTestGroup(TestGroupType.SAMPLES, (short)0, 2);
        table.putTestGroup(TestGroupType.PRETESTS, (short)4, 1);
        table.putTestGroup(TestGroupType.TESTS_1, (short)6, 1);
        return table;
    }
    
    private static class ConsoleResultHandler implements TestResultHandler {

        @Override
        public void process(TestingInfo info) {
            System.out.println("Submission id: " + info.getCodeFileSupplier().getFolder().getFileName());
            System.out.println("Verdict: " + info.getVerdictInfo().getVerdict());
            System.out.println("Points: " + info.getVerdictInfo().getPoints());
            System.out.println("Wrong test number: " + info.getVerdictInfo().getWrongTestNumber());
            System.out.println();
        }
    
    }

}