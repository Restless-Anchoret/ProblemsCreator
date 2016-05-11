package com.ran.testing.temp;

import com.ran.testing.system.ProblemFileSupplier;
import com.ran.testing.system.TestGroupType;
import java.nio.file.Path;

public class SimpleProblemFileSupplier implements ProblemFileSupplier {

    private String problemFolder;
    private SimpleFileSupplier fileSupplier;

    public SimpleProblemFileSupplier(SimpleFileSupplier fileSupplier, String problemFolder) {
        this.problemFolder = problemFolder;
        this.fileSupplier = fileSupplier;
    }
    
    @Override
    public Path getTestInputFile(TestGroupType type, int testNumber) {
        return fileSupplier.getTestInputFile(problemFolder, type, testNumber);
    }

    @Override
    public Path getTestAnswerFile(TestGroupType type, int testNumber) {
        return fileSupplier.getTestAnswerFile(problemFolder, type, testNumber);
    }

}