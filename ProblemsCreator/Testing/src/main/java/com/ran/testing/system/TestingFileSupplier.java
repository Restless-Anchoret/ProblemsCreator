package com.ran.testing.system;

import java.nio.file.Path;

public interface TestingFileSupplier {

    Path getTempFile();
    void deleteTempFile(Path path);
    void deleteAllTempFiles();
    Path getConfigurationFolder();

}