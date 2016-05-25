package com.ran.testing.checker;

import com.ran.testing.system.ProblemFileSupplier;
import com.ran.testing.system.Verdict;
import java.nio.file.Path;

public interface Checker {

    default void initialize(ProblemFileSupplier problemFileSupplier) { }
    Verdict check(Path inputPath, Path outputPath, Path answerPath);

}