package com.ssg.ssg.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Calendar;

import static com.ssg.ssg.utils.DirectoryUtils.dirListFiles;
import static com.ssg.ssg.utils.FileUtils.createMdFilesList;

public class ConversionUtils {

    private static final Path tmpFilePath = Path.of("./html/tmp/tmp.html");

    public static void convertIntoHtmlFile(MdFile mdFile, Path dirOut, String title, String theme) {
        try {
            var fileLines = Files.readAllLines(Path.of(mdFile.filePath()));
            if (fileLines.isEmpty()) {
                throw new NoSuchFileException("Info: File " + mdFile.filePath() + " empty. Not converting.");
            }
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            if (!Files.exists(tmpFilePath)) {
                Files.createFile(tmpFilePath);
                Files.writeString(tmpFilePath, """
                                                <!DOCTYPE html>
                                                <html lang="en">
                                                <head>
                                                <meta charset="UTF-8" />
                                                <meta name="viewport" content="width=device-width, initial-scale=1" />
                                                <meta name="color-scheme" content="light dark" />
                                                <link
                                        rel="stylesheet"
                                """ + "href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico." + theme + ".min.css\"" + """
                                />
                                <title>
                                """ + title + """
                                </title>
                                    </head>
                                        <header>
                                            <nav style="padding:0rem 1rem">
                                                <ul>
                                                    <li><strong>
                                """ + title + """
                                </strong></li>
                                                </ul>
                                            <ul>
                                                <li><a href="/">Home</a></li>
                                            </ul>
                                        </nav>
                                    </header>
                                <body>
                                <main style="padding: 1rem;">
                                """
                        , StandardOpenOption.APPEND);
            }
            for (var line : fileLines) {
                Node document = parser.parse(line);
                Files.writeString(tmpFilePath, renderer.render(document), StandardOpenOption.APPEND);
            }
            Files.writeString(tmpFilePath, """
                    </body>
                        <footer>
                            <hr />
                            <p style="text-align: center;">Created with <a href="https://github.com/Nastro-A/java-ssg">java-ssg</a> and <a href="https://picocss.com/">picocss</a></p>
                        </footer>
                    </html>
                    """, StandardOpenOption.APPEND);
            var htmlPath = Path.of(dirOut + "/" + mdFile.label() + ".html");
            Files.move(tmpFilePath, htmlPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Info: Document " + htmlPath + " generated");
        } catch (NoSuchFileException e) {
            System.out.println("Info: File " + mdFile.filePath() + " empty. Not converting.");
        } catch (IOException e) {
            System.out.println("Error: File " + mdFile.filePath() + " not accessible!");
            System.exit(1);
        }
    }

    public static void convertAllIntoHtmlFiles(Path dirIn, Path dirOut, String title, String theme) {
        var mdList = createMdFilesList(dirListFiles(dirIn));
        for (var mdFile : mdList) {
            try {
                if (Files.getLastModifiedTime(Path.of(mdFile.filePath())).compareTo(Files.getLastModifiedTime(Path.of(mdList.getLast().filePath()))) >= 0) {
                    convertIntoHtmlFile(mdFile, dirOut, title, theme);
                }
            } catch (IOException e) {
                System.out.println("Error: File " + mdFile.filePath() + " not accessible!");
                System.exit(1);
            }
        }
    }
}
