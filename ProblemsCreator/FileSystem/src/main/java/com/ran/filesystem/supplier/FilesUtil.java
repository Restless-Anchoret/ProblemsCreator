package com.ran.filesystem.supplier;

import com.ran.filesystem.logging.FileSystemLogging;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class FilesUtil {

    private FilesUtil() { }
    
    public static String getFileNameWithoutExtension(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
    
    public static boolean checkFolderExists(Path path) {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
            return true;
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE,
                    "IOException while creating folders for path: " + path, exception);
            return false;
        }
    }
    
    public static void processFolderContent(Path folderPath, PathConsumer consumer) {
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(folderPath)) {
            for (Path path: directory) {
                consumer.process(path);
            }
        } catch (Exception exception) {
            FileSystemLogging.logger.log(Level.FINE,
                    "Exception while watching folders for path: " + folderPath, exception);
        }
    }
    
    public static int getMaximumFolderNameNumber(Path folderPath) {
        if (Files.notExists(folderPath)) {
            return 0;
        }
        IntegerKeeper keeper = new IntegerKeeper();
        processFolderContent(folderPath, path -> {
            if (Files.isDirectory(path)) {
                try {
                    int folderNumber = Integer.parseInt(path.getFileName().toString());
                    keeper.setNumber(Math.max(keeper.getNumber(), folderNumber));
                } catch (NumberFormatException exception) { }
            }
        });
        return keeper.getNumber();
    }
    
    @FunctionalInterface
    public static interface PathConsumer {
        void process(Path path) throws Exception;
    }
    
    private static class IntegerKeeper {
        private int number = 0;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }        
    }
    
}