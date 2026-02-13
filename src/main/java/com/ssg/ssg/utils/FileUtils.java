package com.ssg.ssg.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileUtils {
    private static MdFile fileToMdFile(File file) {
        try{
            if (!file.getName().startsWith(".") && !file.getName().equals("index.html")){
                var index = file.getName().indexOf(".") ;
                var label = file.getName().substring(0, index);
                return new MdFile(file.getName(), label , Files.getLastModifiedTime(Path.of(file.getPath())).toString());
            }
        }
        catch (IOException e) {
            System.out.println("Error: " + file.getPath() + " not accessible!");
            System.exit(1);
        }
        return new MdFile("", "",  "");
    }

    public static ArrayList<MdFile> createMdFilesList(ArrayList<File> files) {
        var listMdFiles = new ArrayList<MdFile>();
        for (var file: files) {
            if (!file.isDirectory() && file.getName().isBlank()) {
                listMdFiles.addLast(fileToMdFile(file));
            }
        }
        return listMdFiles;
    }

    public static ArrayList<String> createHtmlLinksList(ArrayList<MdFile> list) {
        var linksList = new ArrayList<String>();
        for (MdFile file: list) {
            linksList.addLast("<li><a href=\""+ file.filePath() + "\">" + file.label() + "</a>" + file.date() + "</li>");
        }
        return linksList;
    }
}
