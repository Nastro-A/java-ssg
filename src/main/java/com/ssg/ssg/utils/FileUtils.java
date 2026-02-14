package com.ssg.ssg.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileUtils {
    private static MdFile fileToMdFile(File file) {
        try {
            if (file.getName().endsWith(".md") && !file.isDirectory()) {
                var index = file.getName().indexOf(".");
                var label = file.getName().substring(0, index);
                return new MdFile(file.getCanonicalPath(), label, Files.getLastModifiedTime(Path.of(file.getCanonicalPath())).toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        } catch (IOException e) {
            System.out.println("Error: " + file.getPath() + " not accessible!");
            System.exit(1);
        }
        return new MdFile("", "", "");
    }

    private static HtmlFile fileToHtmlFile(File file) {
        try {
            if (file.getName().endsWith(".html") && !file.isDirectory()) {
                var index = file.getName().indexOf(".");
                var label = file.getName().substring(0, index);
                return new HtmlFile(file.getCanonicalPath(), label, Files.getLastModifiedTime(Path.of(file.getCanonicalPath())).toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        } catch (IOException e) {
            System.out.println("Error: " + file.getPath() + " not accessible!");
            System.exit(1);
        }
        return new HtmlFile("", "", "");
    }

    public static ArrayList<MdFile> createMdFilesList(ArrayList<File> files) {
        var listMdFiles = new ArrayList<MdFile>();
        for (var file : files) {
            if (!file.getName().startsWith(".") && !file.getName().isBlank() && file.getName().endsWith(".md") && !file.isDirectory()) {
                listMdFiles.addLast(fileToMdFile(file));
            }
        }
        return listMdFiles;
    }

    public static ArrayList<HtmlFile> createHtmlFilesList(ArrayList<File> files) {
        var listHtmlFiles = new ArrayList<HtmlFile>();
        for (var file : files) {
            if (!file.getName().startsWith(".") && !file.getName().isBlank() && file.getName().endsWith(".html") && !file.isDirectory()) {
                listHtmlFiles.addLast(fileToHtmlFile(file));
            }
        }
        return listHtmlFiles;
    }

    public static ArrayList<String> createHtmlLinksList(ArrayList<HtmlFile> list) {
        var linksList = new ArrayList<String>();
        for (HtmlFile file : list) {
            if (!file.filePath().endsWith("index.html")) {
                linksList.addLast("<li><a href=\"" + file.filePath() + "\">" + file.label() + "</a> - " + file.date() + "</li>");
            }
        }
        return linksList;
    }
}
