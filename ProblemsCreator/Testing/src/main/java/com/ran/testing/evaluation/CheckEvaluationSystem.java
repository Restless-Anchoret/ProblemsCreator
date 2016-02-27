package com.ran.testing.evaluation;

import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.Verdict;
import com.ran.testing.system.VerdictInfo;
import java.util.Date;
import java.util.TreeMap;

public class CheckEvaluationSystem implements EvaluationSystem {

    @Override
    public void orderTesting(TesterDelegate delegate, boolean pretestsOnly) {
        for (TestGroupType type: TestGroupType.values()) {
            delegate.performTestGroup(type, false);
        }
    }

    @Override
    public VerdictInfo getVerdictInfo(TestTable testTable, boolean pretestsOnly) {
        Integer decisionTime = null;
        Short decisionMemory = null;
        int testsAccepted = 0;
        
        for (TestGroupType type: TestGroupType.values()) {
            int testsInGroup = testTable.getTestsQuantity(type);
            for (int testNumber = 1; testNumber <= testsInGroup; testNumber++) {
                VerdictInfo verdictInfo = testTable.getVerdictInfoForTest(type, testNumber);
                if (verdictInfo.getVerdict() != Verdict.ACCEPTED) {
                    return verdictInfo.clone().setWrongTestNumber(testsAccepted + testNumber);
                }
                if (verdictInfo.getDecisionTime() != null) {
                    decisionTime = (decisionTime == null ? verdictInfo.getDecisionTime() :
                            Math.max(decisionTime, verdictInfo.getDecisionTime()));
                }
                if (verdictInfo.getDecisionMemory() != null) {
                    decisionMemory = (decisionMemory == null ? verdictInfo.getDecisionMemory() :
                            (short)Math.max(decisionMemory, verdictInfo.getDecisionMemory()));
                }
            }
            testsAccepted += testsInGroup;
        }
        return new VerdictInfo(Verdict.ACCEPTED)
                .setDecisionTime(decisionTime)
                .setDecisionMemory(decisionMemory);
    }

    @Override
    public ProblemResult countProblemResult(TreeMap<Date, VerdictInfo> verdictsMap, Date competitionBeginning) {
        return new ProblemResult((short)0, 0);
    }

}