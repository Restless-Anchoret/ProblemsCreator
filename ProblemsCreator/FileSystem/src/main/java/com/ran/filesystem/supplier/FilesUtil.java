package com.ran.filesystem.supplier;

import com.ran.filesystem.logging.FileSystemLogging;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class FilesUtil {

    private FilesUtil() { }
    
    public static String getFileNameWithoutExtension(Path path) {
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
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
    
    public static List<Path> addNewNumberSubfolders(Path folderPath, int foldersQuantity) {
        if (!FilesUtil.checkFolderExists(folderPath)) {
            return null;
        }
        List<Path> subfolderPaths = new ArrayList<>();
        int maximumNumber = getMaximumFolderNameNumber(folderPath);
        for (int i = 1; i <= foldersQuantity; i++) {
            String newFolderName = Integer.toString(maximumNumber + i);
            Path newFolderPath = Paths.get(folderPath.toString(), newFolderName);
            subfolderPaths.add(newFolderPath);
            if (!checkFolderExists(newFolderPath)) {
                for (Path path: subfolderPaths) {
                    deleteRecursively(path);
                }
                return null;
            }
        }
        return subfolderPaths;
    }
    
    public static Path addNewNumberSubfolder(Path folderPath) {
        List<Path> paths = addNewNumberSubfolders(folderPath, 1);
        if (paths == null) {
            return null;
        }
        return paths.get(0);
    }
    
    public static List<String> getSubfolderNames(Path folderPath) {
        List<String> folderNames = new ArrayList<>();
        if (!FilesUtil.checkFolderExists(folderPath)) {
            return folderNames;
        }
        FilesUtil.processFolderContent(folderPath, path -> {
            if (Files.isDirectory(path)) {
                folderNames.add(path.getFileName().toString());
            }
        });
        sortFolderNames(folderNames);
        return folderNames;
    }
    
    private static void sortFolderNames(List<String> folderNames) {
        List<Integer> numberedFolders = new ArrayList<>();
        List<String> otherFolders = new ArrayList<>();
        for (String name: folderNames) {
            try {
                numberedFolders.add(Integer.parseInt(name));
            } catch (NumberFormatException exception) {
                otherFolders.add(name);
            }
        }
        Collections.sort(numberedFolders);
        Collections.sort(otherFolders);
        folderNames.clear();
        for (Integer number: numberedFolders) {
            folderNames.add(number.toString());
        }
        folderNames.addAll(otherFolders);
    }
    
    public static void normailizeSubfolderNames(Path folderPath) {
        List<String> folderNames = getSubfolderNames(folderPath);
        for (int i = 0; i < folderNames.size(); i++) {
            Path subfolderPath = Paths.get(folderPath.toString(), folderNames.get(i));
            Path newSubfolderPath = Paths.get(folderPath.toString(), Integer.toString(i + 1));
            try {
                Files.move(subfolderPath, newSubfolderPath);
            } catch (IOException exception) {
                FileSystemLogging.logger.log(Level.FINE, "IOException while renaming folders", exception);
            }
        }
    }
    
    public static void deleteRecursively(Path pathToDelete) {
        if (Files.isDirectory(pathToDelete)) {
            processFolderContent(pathToDelete, path -> {
                deleteRecursively(path);
            });
        }
        try {
            Files.deleteIfExists(pathToDelete);
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE,
                    "IOException while deleting for path: " + pathToDelete, exception);
        }
    }
    
    public static String getFileDescription(Path path) {
        if (path == null) {
            return null;
        }
        String description = path.getFileName().toString();
        try {
            long fileSize = Files.size(path);
            if (fileSize < 1000) {
                description += " (" + fileSize + " bytes)";
            } else {
                description += " (" + (fileSize / 1000) + " KB)";
            }
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE,
                    "IOException while getting file size: " + path, exception);
        }
        return description;
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