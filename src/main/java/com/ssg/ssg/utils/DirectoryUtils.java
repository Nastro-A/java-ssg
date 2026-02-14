package com.ssg.ssg.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class DirectoryUtils {


    public static boolean ifDirNotExistentCreate(Path dirPath) throws IOException {
        if (Files.notExists(dirPath)) {
            try {
                Files.createDirectory(dirPath);
                System.out.println("Directory at " + dirPath + " created.");
                return ifDirExistCreateTmp(dirPath);
            } catch (IOException e) {
                throw new IOException("Error creating " + dirPath + " directory!");
            }
        } else {
            return ifDirExistsCheckEmptinessAndCreateTmp(dirPath);
        }
    }

    public static boolean ifDirExistsCheckEmptinessAndCreateTmp(Path dirPath) throws IOException {
        if (Files.isDirectory(dirPath)) {
            try (Stream<Path> entries = Files.list(dirPath)) {
                if (entries.findAny().isPresent()) {
                    throw new DirectoryNotEmptyException(dirPath.toString());
                } else {
                    System.out.println("Directory not created, already exists and is empty.");
                    return ifDirExistCreateTmp(dirPath);
                }
            } catch (DirectoryNotEmptyException e) {
                try (Stream<Path> entries = Files.list(dirPath)) {
                    if (!entries.toList().contains(Path.of(dirPath + "/tmp"))) {
                        throw new IOException("Error: Selected directory not empty!");
                    }
                    return true;
                }
            } catch (IOException e) {
                throw new IOException("Error: Cannot open directory. Do it have the right permissions?");
            }
        } else {
            throw new IOException("Path links to a file not a directory!");
        }
    }

    public static boolean ifDirExistCreateTmp(Path dirPath) throws IOException {
        try {
            dirPath = Files.createDirectory(Path.of(dirPath + "/tmp"));
        } catch (IOException e) {
            throw new IOException("Error creating " + dirPath + " directory!");
        }
        System.out.println("Info: Directory at " + dirPath + " created.");
        return true;
    }

    public static ArrayList<File> dirListFiles(Path dirPath){
        if (Files.exists(dirPath) && Files.isDirectory(dirPath)){
            // Iterate on the directory to extract the Files
            var list = new ArrayList<File>();
            try (var streamFiles = Files.list(dirPath)) {
                var filesPath = streamFiles.toList();
                for (var filePath : filesPath) {
                        list.addLast(filePath.toFile());
                }
                return list;
            } catch (IOException e){
                System.out.println("Error: " + dirPath + " not accessible!");
                System.exit(1);
            }
        }
        return new ArrayList<>();
    }
}
