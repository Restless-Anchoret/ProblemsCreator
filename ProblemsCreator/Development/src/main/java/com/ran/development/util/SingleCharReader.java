package com.ran.development.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class SingleCharReader {

    private static final int QUEUE_SIZE = 1000;
    
    private BufferedReader reader;
    private Deque<Integer> deque = new LinkedList<>();

    public SingleCharReader(BufferedReader reader) {
        this.reader = reader;
    }
    
    public int next() throws IOException {
        updateQueue();
        if (deque.peekFirst() == -1) {
            return -1;
        }
        return deque.removeFirst();
    }
    
    public int peek() throws IOException {
        updateQueue();
        return deque.peekFirst();
    }
    
    private void updateQueue() throws IOException {
        while (deque.size() < QUEUE_SIZE && !Objects.equals(deque.peekLast(), -1)) {
            int firstSymbol = reader.read();
            if (firstSymbol != -1 && (char)firstSymbol == '\r') {
                int secondSymbol = reader.read();
                if (secondSymbol == -1 || (char)secondSymbol != '\n') {
                    deque.addLast(firstSymbol);
                }
                deque.addLast(secondSymbol);
            } else {
                deque.addLast(firstSymbol);
            }
        }
    }
    
}