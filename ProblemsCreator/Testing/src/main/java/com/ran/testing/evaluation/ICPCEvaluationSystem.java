package com.ran.testing.evaluation;

import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.Verdict;
import com.ran.testing.system.VerdictInfo;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ICPCEvaluationSystem implements EvaluationSystem {

    private static final int FINE_FOR_FAILURE = 20;
    
    @Override
    public void orderTesting(TesterDelegate delegate, boolean pretestsOnly) {
        int lastGroupIndex = (pretestsOnly ? TestGroupType.PRETESTS.ordinal() :
                TestGroupType.values().length - 1);
        TestGroupType[] types = Arrays.copyOfRange(TestGroupType.values(), 0, lastGroupIndex);
        for (TestGroupType type: types) {
            Integer wrongTestNumber = delegate.performTestGroup(type, true);
            if (wrongTestNumber != null) {
                break;
            }
        }
    }
    
    @Override
    public VerdictInfo getVerdictInfo(TestTable testTable, boolean pretestsOnly) {
        Integer decisionTime = null;
        Short decisionMemory = null;
        int testsAccepted = 0;
        
        int lastGroupIndex = (pretestsOnly ? TestGroupType.PRETESTS.ordinal() :
                TestGroupType.values().length - 1);
        TestGroupType[] types = Arrays.copyOfRange(TestGroupType.values(), 0, lastGroupIndex);
        
        for (TestGroupType type: types) {
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
        Verdict verdict = (pretestsOnly ? Verdict.PRETESTS_ACC : Verdict.ACCEPTED);
        return new VerdictInfo(verdict)
                .setDecisionTime(decisionTime)
                .setDecisionMemory(decisionMemory);
    }

    @Override
    public ProblemResult countProblemResult(TreeMap<Date, VerdictInfo> verdictsMap,
            Date competitionBeginning) {
        int failureCounter = 0;
        for (Map.Entry<Date, VerdictInfo> entry: verdictsMap.entrySet()) {
            Verdict verdict = entry.getValue().getVerdict();
            if (verdict != Verdict.ACCEPTED && verdict != Verdict.PRETESTS_ACC) {
                failureCounter++;
            } else {
                int minutesAfterBeginning = (int)((entry.getKey().getTime()
                        - competitionBeginning.getTime()) / 60_000);
                return new ProblemResult((short)1, failureCounter * FINE_FOR_FAILURE +
                        minutesAfterBeginning);
            }
        }
        return new ProblemResult((short)0, 0);
    }

}