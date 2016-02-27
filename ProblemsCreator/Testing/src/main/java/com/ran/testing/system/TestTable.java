package com.ran.testing.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTable {

    private Map<TestGroupType, Short> pointsForTestMap = new HashMap<>();
    private Map<TestGroupType, List<VerdictInfo>> verdictsMap = new HashMap<>();

    public void putTestGroup(TestGroupType type, short pointsForTest, int testsQuantity) {
        pointsForTestMap.put(type, pointsForTest);
        List<VerdictInfo> verdictsList = new ArrayList<>(testsQuantity);
        for (int i = 0; i < testsQuantity; i++) {
            verdictsList.add(VerdictInfo.VERDICT_NOT_TESTED);
        }
        verdictsMap.put(type, verdictsList);
    }

    public short getPointsForTest(TestGroupType type) {
        if (verdictsMap.containsKey(type)) {
            return pointsForTestMap.get(type);
        } else {
            return (short)0;
        }
    }
    
    public boolean containsGroup(TestGroupType type) {
        return verdictsMap.containsKey(type);
    }
    
    public int getTestsQuantity(TestGroupType type) {
        if (verdictsMap.containsKey(type)) {
            return verdictsMap.get(type).size();
        } else {
            return 0;
        }
    }

    public VerdictInfo getVerdictInfoForTest(TestGroupType type, int testNumber) {
        return verdictsMap.get(type).get(testNumber - 1);
    }

    public void putVerdictInfo(TestGroupType type, int testNumber, VerdictInfo info) {
        verdictsMap.get(type).set(testNumber - 1, info);
    }

}