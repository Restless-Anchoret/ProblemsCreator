package com.ran.filesystem.supplier;

import com.ran.filesystem.logging.FileSystemLogging;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class StandardCodeSupplier implements CodeSupplier {

    private static final String SOURCE_FOLDER = "src",
                                COMPILING_FOLDER = "bin";
    
    private Path folderPath;

    public StandardCodeSupplier(Path folderPath) {
        this.folderPath = folderPath;
    }
    
    @Override
    public Path getFolder() {
        return folderPath;
    }

    @Override
    public Path getSourceFolder() {
        Path sourceFolderPath = Paths.get(folderPath.toString(), SOURCE_FOLDER);
        if (!FilesUtil.checkFolderExists(sourceFolderPath)) {
            return null;
        }
        return sourceFolderPath;
    }

    @Override
    public Path getSourceFile() {
        Path sourceFolderPath = getSourceFolder();
        if (sourceFolderPath == null) {
            return null;
        }
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(sourceFolderPath)) {
            for (Path path: directory) {
                return path;
            }
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while watching source files folder", exception);
        }
        return null;
    }
    
    @Override
    public Path putSourceFile(Path sourceFile) {
        Path sourceFolderPath = getSourceFolder();
        try {
            Files.copy(sourceFile, sourceFolderPath.resolve(sourceFile.getFileName()));
            return getSourceFile();
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while copying file", exception);
            return null;
        }
    }

    @Override
    public Path getCompileFolder() {
        Path compileFolderPath = Paths.get(folderPath.toString(), COMPILING_FOLDER);
        if (!FilesUtil.checkFolderExists(compileFolderPath)) {
            return null;
        }
        return compileFolderPath;
    }

    @Override
    public Path getCompileFile() {
        Path compileFolderPath = getCompileFolder();
        Path sourceFilePath = getSourceFile();
        if (compileFolderPath == null || sourceFilePath == null) {
            return null;
        }
        String fileName = FilesUtil.getFileNameWithoutExtension(sourceFilePath);
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(compileFolderPath)) {
            for (Path path: directory) {
                if (FilesUtil.getFileNameWithoutExtension(path).equals(fileName)) {
                    return path;
                }
            }
        } catch (IOException exception) {
            FileSystemLogging.logger.log(Level.FINE, "IOException while watching source files folder", exception);
        }
        return null;
    }

}