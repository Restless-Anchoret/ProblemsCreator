package com.ran.interaction.support;

import com.ran.testing.system.TestGroupType;
import com.ran.testing.system.TestTable;
import com.ran.testing.system.VerdictInfo;
import java.util.ArrayList;
import java.util.List;

public class TestTableWrapper {

    private TestTable testTable;
    private List<TestGroupWithNumber> testsList = new ArrayList<>();
    
    public TestTableWrapper(TestTable testTable) {
        this.testTable = testTable;
        initialize();
    }
    
    private void initialize() {
        for (TestGroupType type: TestGroupType.values()) {
            int quantity = testTable.getTestsQuantity(type);
            for (int i = 1; i <= quantity; i++) {
                testsList.add(new TestGroupWithNumber(type, i));
            }
        }
    }

    public TestTable getTestTable() {
        return testTable;
    }
    
    public int getTestsQuantity() {
        return testsList.size();
    }
    
    public List<Integer> getCommonTestNumbers() {
        List<Integer> list = new ArrayList<>(getTestsQuantity());
        for (int i = 1; i <= getTestsQuantity(); i++) {
            list.add(i);
        }
        return list;
    }
    
    public int getTestNumber(int commonTestNumber) {
        return testsList.get(commonTestNumber - 1).number;
    }
    
    public TestGroupType getTestGroupType(int commonTestNumber) {
        return testsList.get(commonTestNumber - 1).testGroupType;
    }
    
    public VerdictInfo getVerdictInfo(int commonTestNumber) {
        return testTable.getVerdictInfoForTest(getTestGroupType(commonTestNumber),
                getTestNumber(commonTestNumber));
    }
    
    private static class TestGroupWithNumber {
        public TestGroupType testGroupType;
        public int number;

        public TestGroupWithNumber(TestGroupType testGroupType, int number) {
            this.testGroupType = testGroupType;
            this.number = number;
        }
    }
    
}