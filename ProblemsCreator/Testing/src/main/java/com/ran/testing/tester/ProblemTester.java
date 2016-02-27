package com.ran.testing.tester;

import com.ran.testing.system.TestingFileSupplier;
import com.ran.testing.system.TestingInfo;

public interface ProblemTester {

    void performTesting(TestingFileSupplier fileSupplier, TestingInfo info);

}