package com.ran.filesystem.supplier;

import java.nio.file.Path;

public interface CodeSupplier {

    Path getFolder();
    Path getSourceFolder();
    Path getSourceFile();
    Path putSourceFile(Path sourceFile);
    Path getCompileFolder();
    Path getCompileFile();

}
