package com.ran.interaction.support;

import com.ran.filesystem.supplier.FileSupplier;
import com.ran.testing.system.ProblemFileSupplier;
import com.ran.testing.system.TestGroupType;
import java.nio.file.Path;

public class ProblemFileSupplierImpl implements ProblemFileSupplier {

    private String problemFolder;
    private FileSupplier fileSupplier;

    public ProblemFileSupplierImpl(String problemFolder, FileSupplier fileSupplier) {
        this.problemFolder = problemFolder;
        this.fileSupplier = fileSupplier;
    }
    
    @Override
    public Path getTestInputFile(TestGroupType type, int testNumber) {
        return fileSupplier.getTestInputFile(problemFolder, type.name().toLowerCase(), testNumber);
    }

    @Override
    public Path getTestAnswerFile(TestGroupType type, int testNumber) {
        return fileSupplier.getTestAnswerFile(problemFolder, type.name().toLowerCase(), testNumber);
    }

    @Override
    public Path getCheckerClassFile() {
        return fileSupplier.getCheckerCodeSupplier(problemFolder).getCompileFile();
    }

}