package com.ran.filesystem.descriptor;

import java.nio.file.Path;

public class TestGroupDescriptor extends EntityDescriptor {

    private static final String ROOT_ELEMENT = "testGroup",
                                POINTS_FOR_TEST = "pointsForTest";
    
    public static TestGroupDescriptor getEmptyTestGroupDescriptor(Path path) {
        TestGroupDescriptor descriptor = new TestGroupDescriptor(path);
        descriptor.persist();
        return descriptor;
    }

    public static TestGroupDescriptor getExistingTestGroupDescriptor(Path path) {
        TestGroupDescriptor descriptor = new TestGroupDescriptor(path);
        descriptor.load();
        return descriptor;
    }

    private TestGroupDescriptor(Path path) {
        super(path, ROOT_ELEMENT);
        setProperty(POINTS_FOR_TEST, "");
    }
    
    public Integer getPointsForTest() {
        return parseInt(getProperty(POINTS_FOR_TEST));
    }
    
    public void setPointsForTest(Integer points) {
        setProperty(POINTS_FOR_TEST, intToString(points));
    }
    
}