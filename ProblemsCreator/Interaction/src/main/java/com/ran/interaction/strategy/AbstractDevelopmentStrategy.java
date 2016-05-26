package com.ran.interaction.strategy;

import com.ran.development.logging.DevelopmentLogging;
import com.ran.filesystem.supplier.CodeSupplier;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.testing.language.FailException;
import com.ran.testing.language.LanguageToolkit;
import com.ran.testing.language.LanguageToolkitRegistry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDevelopmentStrategy implements DevelopmentStrategy {

    private LanguageToolkit languageToolkit = LanguageToolkitRegistry.registry().getDefault();
    private FileSupplier fileSupplier = null;
    private CodeSupplier codeSupplier = null;
    private Consumer<String> outputConsumer = null;
    private Thread developmentThread = null;
    
    @Override
    public boolean isNeedSaveAbility() {
        return false;
    }

    @Override
    public String getExecutingFileName() {
        return codeSupplier.getSourceFile().getFileName().toString();
    }

    public LanguageToolkit getLanguageToolkit() {
        return languageToolkit;
    }

    @Override
    public void setLanguageToolkit(LanguageToolkit languageToolkit) {
        this.languageToolkit = languageToolkit;
    }

    @Override
    public void setFileSupplier(FileSupplier fileSupplier) {
        this.fileSupplier = fileSupplier;
    }

    public FileSupplier getFileSupplier() {
        return fileSupplier;
    }

    @Override
    public void setCodeSupplier(CodeSupplier codeSupplier) {
        this.codeSupplier = codeSupplier;
    }

    public CodeSupplier getCodeSupplier() {
        return codeSupplier;
    }

    @Override
    public void setOutputConsumer(Consumer<String> outputConsumer) {
        this.outputConsumer = outputConsumer;
    }

    public Consumer<String> getOutputConsumer() {
        return outputConsumer;
    }

    @Override
    public void runDevelopmentProcess() {
        developmentThread = new Thread(() -> {
            int compilationResult = compileSafety();
            if (compilationResult == 0) {
                performDevelopmentProcess();
            }
        });
        developmentThread.start();
    }

    @Override
    public void interruptDevelopmentProcess() {
        developmentThread.interrupt();
    }

    @Override
    public void saveDevelopmentResults() { }
    
    private int compileSafety() {
        int compilationResult = 1;
        String errorMessages = null;
        try (ByteArrayOutputStream errorStream = new ByteArrayOutputStream()) {
            compilationResult = languageToolkit.compile(codeSupplier.getSourceFile(),
                    codeSupplier.getCompileFolder(), fileSupplier.getConfigurationFolder(), errorStream);
            errorMessages = errorStream.toString();
        } catch (FailException exception) {
            DevelopmentLogging.logger.log(Level.FINE, exception.getMessage(), exception);
        } catch (IOException exception) {
            DevelopmentLogging.logger.log(Level.FINE, "IOException while compilation", exception);
        }
        if (compilationResult == 0) {
            outputConsumer.accept("Compilation has completed successfully.");
        } else {
            outputConsumer.accept("Error while code compilation.");
            outputConsumer.accept(errorMessages + "\n");
        }
        return compilationResult;
    }
    
    protected abstract void performDevelopmentProcess();

}