package com.ran.testing.checker;

import com.ran.testing.logging.TestingLogging;
import com.ran.testing.system.Verdict;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.logging.Level;

public class MatchChecker implements Checker {
    
    @Override
    public Verdict check(Path inputPath, Path outputPath, Path answerPath) {
        if (Files.notExists(inputPath) || Files.notExists(outputPath)) {
            TestingLogging.logger.fine("Input or output files not found");
            return Verdict.FAIL;
        }
        try (Tokenizer outputTokenizer = new Tokenizer(outputPath);
                Tokenizer answerTokenizer = new Tokenizer(answerPath)) {
            do {
                String outputToken = outputTokenizer.nextToken();
                String answerToken = answerTokenizer.nextToken();
                if (outputToken == null && answerToken == null) {
                    return Verdict.ACCEPTED;
                } else if (!Objects.equals(outputToken, answerToken)) {
                    return Verdict.WRONG_ANSWER;
                }
            } while (true);
        } catch (IOException exception) {
            TestingLogging.logger.log(Level.FINE,
                    "Exception while reading input or output files", exception);
            return Verdict.FAIL;
        }
    }
    
    private static class Tokenizer implements Closeable {

        private BufferedReader reader;
        private StringTokenizer tokenizer = null;
        
        public Tokenizer(Path path) throws IOException {
            reader = new BufferedReader(new InputStreamReader(Files.newInputStream(
                    path, StandardOpenOption.READ)));
        }
        
        public String nextToken() throws IOException {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                String nextLine = null;
                do {
                    nextLine = reader.readLine();
                } while (Objects.equals(nextLine, ""));
                if (nextLine == null) {
                    return null;
                }
                tokenizer = new StringTokenizer(nextLine);
            }
            return tokenizer.nextToken();
        }
        
        @Override
        public void close() throws IOException {
            reader.close();
        }
        
    }
    
}