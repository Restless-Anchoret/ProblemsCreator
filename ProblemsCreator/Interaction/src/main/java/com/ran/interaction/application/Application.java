package com.ran.interaction.application;

import com.ran.filesystem.supplier.FileSupplier;
import com.ran.filesystem.supplier.StandardFileSupplier;
import com.ran.interaction.support.TestingFileSupplierImpl;
import com.ran.testing.system.MultithreadTestingSystem;
import com.ran.testing.system.TestingSystem;
import java.nio.file.Paths;

public class Application {

    private static final int TESTING_SYSTEM_THREADS_QUANTITY = 10;
    
    private TestingSystem testingSystem;
    private FileSupplier fileSupplier;
    
    public void init() {
        fileSupplier = new StandardFileSupplier(Paths.get(System.getProperty("user.dir")));
        MultithreadTestingSystem multithreadTestingSystem =
                MultithreadTestingSystem.getDefault();
        multithreadTestingSystem.setThreadsQuantity(TESTING_SYSTEM_THREADS_QUANTITY);
        multithreadTestingSystem.setFileSupplier(new TestingFileSupplierImpl(fileSupplier));
        testingSystem = multithreadTestingSystem;
        testingSystem.start();
    }
    
    public void stop() {
        testingSystem.stop();
    }

    public TestingSystem getTestingSystem() {
        return testingSystem;
    }

    public FileSupplier getFileSupplier() {
        return fileSupplier;
    }
    
}