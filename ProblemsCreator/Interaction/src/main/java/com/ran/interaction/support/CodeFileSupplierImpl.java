package com.ran.interaction.support;

import com.ran.filesystem.supplier.CodeSupplier;
import com.ran.testing.system.CodeFileSupplier;
import java.nio.file.Path;

public class CodeFileSupplierImpl implements CodeFileSupplier {

    private CodeSupplier codeSupplier;

    public CodeFileSupplierImpl(CodeSupplier codeSupplier) {
        this.codeSupplier = codeSupplier;
    }
    
    @Override
    public Path getFolder() {
        return codeSupplier.getFolder();
    }

    @Override
    public Path getSourceFolder() {
        return codeSupplier.getSourceFolder();
    }

    @Override
    public Path getSourceFile() {
        return codeSupplier.getSourceFile();
    }

    @Override
    public Path getCompileFolder() {
        return codeSupplier.getCompileFolder();
    }

    @Override
    public Path getCompileFile() {
        return codeSupplier.getCompileFile();
    }

}