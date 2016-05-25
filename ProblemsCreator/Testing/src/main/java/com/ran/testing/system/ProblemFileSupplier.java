package com.ran.testing.system;

import java.nio.file.Path;

public interface ProblemFileSupplier {

    Path getTestInputFile(TestGroupType type, int testNumber);
    Path getTestAnswerFile(TestGroupType type, int testNumber);
    Path getCheckerClassFile();
    
}