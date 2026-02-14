package com.ssg.ssg.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.ssg.ssg.utils.DirectoryUtils.dirListFiles;
import static com.ssg.ssg.utils.FileUtils.*;

public class IndexHtml {
    public static void createIndex(Path dirHtml, String title, String theme) {
        if (Files.exists(dirHtml) && Files.isDirectory(dirHtml)) {
            var list = dirListFiles(dirHtml);
            var linksList = createHtmlLinksList(createHtmlFilesList(dirListFiles(dirHtml)));
            StringBuilder linksStr = new StringBuilder();
            for (var link : linksList) {
                linksStr.append(link);
            }
            var html = """
                    <!DOCTYPE html>
                    <html lang="en">
                      <head>
                        <meta charset="UTF-8" />
                        <meta name="viewport" content="width=device-width, initial-scale=1" />
                        <meta name="color-scheme" content="light dark" />
                        <link
                          rel="stylesheet"
                    """ +
                    "href=\"https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico." + theme + ".min.css\"" + """    
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
                        <ul>
                    """ + linksStr + """
                    </ul>
                      </body>
                      <footer>
                          <hr />
                          <p style="text-align: center;">Created with <a href="https://github.com/Nastro-A/java-ssg">java-ssg</a> and <a href="https://picocss.com/">picocss</a></p>
                        </footer>
                    </html>
                    """;
            try {
                Files.writeString(Path.of(dirHtml + "/index.html"), html);
            } catch (IOException e) {
                System.out.println("Error: " + dirHtml + " not writable!");
                System.exit(1);
            }
        } else {
            System.out.println("Error: Directory " + dirHtml + " does not exist!");
            System.exit(1);
        }
    }
}
