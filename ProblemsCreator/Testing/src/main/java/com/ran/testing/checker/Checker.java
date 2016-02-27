package com.ran.testing.checker;

import com.ran.testing.system.Verdict;
import java.nio.file.Path;

public interface Checker {

    Verdict check(Path inputPath, Path outputPath, Path answerPath);

}