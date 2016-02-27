package com.ran.testing.evaluation;

import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.Verdict;
import com.ran.testing.system.VerdictInfo;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;

public class IOIEvaluationSystem implements EvaluationSystem {

    @Override
    public void orderTesting(TesterDelegate delegate, boolean pretestsOnly) {
        Integer wrongSampleTest = delegate.performTestGroup(TestGroupType.SAMPLES, true);
        if (wrongSampleTest != null) {
            return;
        }
        int firstGroupIndex = TestGroupType.PRETESTS.ordinal();
        int lastGroupIndex = (pretestsOnly ? TestGroupType.PRETESTS.ordinal() : TestGroupType.values().length - 1);
        TestGroupType[] types = Arrays.copyOfRange(TestGroupType.values(), firstGroupIndex, lastGroupIndex);
        for (TestGroupType type: types) {
            delegate.performTestGroup(type, false);
        }
    }

    @Override
    public VerdictInfo getVerdictInfo(TestTable testTable, boolean pretestsOnly) {
        int samplesQuantity = testTable.getTestsQuantity(TestGroupType.SAMPLES);
        for (int testNumber = 1; testNumber <= samplesQuantity; testNumber++) {
            VerdictInfo verdictInfo = testTable.getVerdictInfoForTest(TestGroupType.SAMPLES, testNumber);
            if (verdictInfo.getVerdict() != Verdict.ACCEPTED) {
                return verdictInfo.clone().setWrongTestNumber(testNumber);
            }
        }
        
        int testsCounted = samplesQuantity;
        Integer wrongTestNumber = null;
        short points = 0;
        
        int firstGroupIndex = TestGroupType.PRETESTS.ordinal();
        int lastGroupIndex = (pretestsOnly ? TestGroupType.PRETESTS.ordinal() : TestGroupType.values().length - 1);
        TestGroupType[] types = Arrays.copyOfRange(TestGroupType.values(), firstGroupIndex, lastGroupIndex);
        
        for (TestGroupType type: types) {
            int testsQuantity = testTable.getTestsQuantity(type);
            short pointsForTest = testTable.getPointsForTest(type);
            for (int testNumber = 1; testNumber <= testsQuantity; testNumber++) {
                VerdictInfo verdictInfo = testTable.getVerdictInfoForTest(type, testNumber);
                if (verdictInfo.getVerdict() == Verdict.ACCEPTED) {
                    points += pointsForTest;
                } else if (wrongTestNumber == null) {
                    wrongTestNumber = testsCounted + testNumber;
                }
            }
            testsCounted += testsQuantity;
        }
        return new VerdictInfo(Verdict.PARTIAL_ACC, points)
                .setWrongTestNumber(wrongTestNumber);
    }

    @Override
    public ProblemResult countProblemResult(TreeMap<Date, VerdictInfo> verdictsMap, Date competitionBeginning) {
        if (verdictsMap.isEmpty()) {
            return new ProblemResult((short)0, 0);
        }
        VerdictInfo lastVerdictInfo = verdictsMap.lastEntry().getValue();
        if (lastVerdictInfo.getVerdict() == Verdict.PARTIAL_ACC) {
            return new ProblemResult(lastVerdictInfo.getPoints(), 0);
        } else {
            return new ProblemResult((short)0, 0);
        }
    }
    
}