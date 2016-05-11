package com.ran.testing.temp;

import com.ran.testing.system.CodeFileSupplier;
import java.nio.file.Path;

public class SimpleCodeFileSupplier implements CodeFileSupplier {

    private String codeFolder;
    private SimpleFileSupplier fileSupplier;

    public SimpleCodeFileSupplier(SimpleFileSupplier fileSupplier, String codeFolder) {
        this.codeFolder = codeFolder;
        this.fileSupplier = fileSupplier;
    }
    
    @Override
    public Path getFolder() {
        return fileSupplier.getSubmissionFolder(codeFolder);
    }

    @Override
    public Path getSourceFolder() {
        return fileSupplier.getSubmissionSourceFolder(codeFolder);
    }

    @Override
    public Path getSourceFile() {
        return fileSupplier.getSubmissionSourceFile(codeFolder);
    }

    @Override
    public Path getCompileFolder() {
        return fileSupplier.getSubmissionCompileFolder(codeFolder);
    }

    @Override
    public Path getCompileFile() {
        return fileSupplier.getSubmissionCompileFile(codeFolder);
    }

}