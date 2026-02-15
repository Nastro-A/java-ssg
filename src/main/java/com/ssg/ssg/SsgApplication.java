package com.ssg.ssg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static com.ssg.ssg.utils.ConversionUtils.convertAllIntoHtmlFiles;
import static com.ssg.ssg.utils.DirectoryUtils.ifDirNotExistentCreate;
import static com.ssg.ssg.utils.IndexHtml.createIndex;

@SpringBootApplication
public class SsgApplication {
    private static Path mdDir;
    private static Path htmlDir;
    public static String picoTheme;
    public static String siteTitle;
    private static Boolean reloadAllFiles;
    static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream input = SsgApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
                String mdDirStr = props.getProperty("com.ssg.mddir");
                String htmlDirStr = props.getProperty("com.ssg.htmldir");
                String picoThemeStr = System.getenv("PICO_THEME");
                String siteTitleStr = System.getenv("SITE_TITLE");
                String reloadAllFilesStr = System.getenv("RELOAD");
                if (mdDirStr == null) {
                    throw new Exception("com.ssg.mddir not set in application.properties");
                }
                System.out.println("INFO: mdDir:" + mdDirStr);
                mdDir = Path.of(mdDirStr);
                if (htmlDirStr == null) {
                    throw new Exception("com.ssg.htmldir not set in application.properties");
                }
                System.out.println("INFO: htmlDir:" + htmlDir);
                htmlDir = Path.of(htmlDirStr);
                if (picoThemeStr == null) {
                    throw new Exception("PICO_THEME not set in ENVIRONMENT");
                }
                System.out.println("INFO: PICO_THEME:" + picoTheme);
                picoTheme = picoThemeStr.toLowerCase();
                if (siteTitleStr == null) {
                    throw new Exception("SITE_TITLE not set in ENVIRONMENT");
                }
                System.out.println("INFO: SITE_TITLE: " + siteTitleStr);
                siteTitle = siteTitleStr;
                if (reloadAllFilesStr == null) {
                    throw new Exception("RELOAD not set in ENVIRONMENT");
                }
                System.out.println("INFO: RELOAD:" + reloadAllFilesStr);
                reloadAllFiles = reloadAllFilesStr.equals("true");
            }
        } catch (IOException e) {
            System.out.println("Error: application.properties file not exists");
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        if (ifDirNotExistentCreate(mdDir) && ifDirNotExistentCreate(htmlDir)) {
            if (reloadAllFiles || Files.getLastModifiedTime(mdDir).compareTo(Files.getLastModifiedTime(htmlDir)) > 0) {
                if (reloadAllFiles){
                    System.out.println("Info: Reconverting all MD files into HTML.");
                }
                convertAllIntoHtmlFiles(mdDir, htmlDir, siteTitle, picoTheme);
            }
            createIndex(htmlDir, siteTitle, picoTheme);
            SpringApplication.run(SsgApplication.class, args);
        }
    }
}
