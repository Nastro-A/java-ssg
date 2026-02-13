package com.ssg.ssg.utils;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DirectoryUtils {


    public static boolean ifDirNotExistentCreateElseOk(Path dirPath) throws IOException {
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
        System.out.println("Directory at " + dirPath + " created.");
        return true;
    }
}
