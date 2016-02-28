package com.ran.development.gen;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

public abstract class Generator {

    private static final long DEFAULT_RANDOM_SEED = 1938572538L;
    
    public static Generator getDefault() {
        return new Generator() {
            @Override
            protected void generate(String[] args) {
            }
        };
    }
    
    private Random random;
    private PrintStream output;
    private RegularExpressionParser parser = new RegularExpressionParser();
    
    public Generator() {
        random = new Random(DEFAULT_RANDOM_SEED);
        output = System.out;
    }
    
    public Generator(OutputStream output, long randomSeed) {
        random = new Random(randomSeed);
        this.output = new PrintStream(new BufferedOutputStream(output), true);
    }
    
    public Generator(OutputStream output) {
        this(output, DEFAULT_RANDOM_SEED);
    }

    public void setRandomSeed(long randomSeed) {
        this.random.setSeed(randomSeed);
    }

    public void setOutput(OutputStream outputStream) {
        this.output = new PrintStream(new BufferedOutputStream(outputStream), true);
    }
    
    protected Random getRandom() {
        return random;
    }
    
    protected PrintStream getOutput() {
        return output;
    }
    
    protected void print(String line) {
        output.print(line);
    }
    
    protected void println(String line) {
        output.println(line);
    }
    
    protected void println() {
        output.println();
    }
    
    protected void print(int number) {
        output.print(number);
    }
    
    protected void print(long number) {
        output.print(number);
    }
    
    protected void print(double number) {
        output.print(number);
    }
    
    protected int nextInt(int higherBound) {
        return random.nextInt(higherBound);
    }
    
    protected int nextInt(int lowerBound, int higherBound) {
        return lowerBound + random.nextInt(higherBound - lowerBound + 1);
    }
    
    protected long nextLong(long higherBound) {
        return Math.abs(random.nextLong()) % higherBound;
    }
    
    protected long nextLong(long lowerBound, long higherBound) {
        return lowerBound + nextLong(higherBound - lowerBound + 1);
    }
    
    protected double nextDouble(double higherBound) {
        return random.nextDouble() * higherBound;
    }
    
    protected double nextDouble(double lowerBound, double higherBound) {
        return lowerBound + random.nextDouble() * (higherBound - lowerBound);
    }
    
    protected boolean nextBoolean() {
        return random.nextBoolean();
    }
    
    protected int any(int[] values) {
        return values[random.nextInt(values.length)];
    }
    
    protected long any(long[] values) {
        return values[random.nextInt(values.length)];
    }
    
    protected double any(double[] values) {
        return values[random.nextInt(values.length)];
    }
    
    protected char any(char[] values) {
        return values[random.nextInt(values.length)];
    }
    
    protected <T> T any(T[] objects) {
        return objects[random.nextInt(objects.length)];
    }
    
    protected char any(String regularExpression) {
        int quantity = parser.getCharactersQuantity(regularExpression);
        return parser.getCharacter(regularExpression, random.nextInt(quantity));
    }
    
    protected String anyLine(String regularExpression, int length) {
        return anyLines(regularExpression, length, 1)[0];
    }
    
    protected String[] anyLines(String regularExpression, int lengthOfLine, int linesQuantity) {
        int quantity = parser.getCharactersQuantity(regularExpression);
        String[] stringsArray = new String[linesQuantity];
        char[] array = new char[lengthOfLine];
        for (int i = 0; i < linesQuantity; i++) {
            for (int j = 0; j < lengthOfLine; j++) {
                array[j] = parser.getCharacter(regularExpression, random.nextInt(quantity));
            }
            stringsArray[i] = new String(array);
        }
        return stringsArray;
    }
    
    abstract protected void generate(String[] args);
    
}