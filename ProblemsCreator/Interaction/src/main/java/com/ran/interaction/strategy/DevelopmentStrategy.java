package com.ran.interaction.strategy;

import com.ran.filesystem.supplier.CodeSupplier;
import com.ran.filesystem.supplier.FileSupplier;
import com.ran.testing.language.LanguageToolkit;
import java.util.function.Consumer;

public interface DevelopmentStrategy {

    boolean isNeedSaveAbility();
    String getExecutingFileName();
    void setLanguageToolkit(LanguageToolkit languageToolkit);
    void setFileSupplier(FileSupplier fileSupplier);
    void setCodeSupplier(CodeSupplier codeSupplier);
    void setOutputConsumer(Consumer<String> outputConsumer);
    void runDevelopmentProcess();
    void interruptDevelopmentProcess();
    void saveDevelopmentResults();
    
}
