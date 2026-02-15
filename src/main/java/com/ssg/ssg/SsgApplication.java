package com.ssg.ssg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.FileInputStream;
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
        try (InputStream input = new FileInputStream("application.properties")) {
            props.load(input);
            var helper = new PropertyPlaceholderHelper("${", "}");
            for (var name : props.stringPropertyNames()) {
                var value = props.getProperty(name);
                var resolvedValue = helper.replacePlaceholders(value, System::getProperty);
                if (resolvedValue.equals(value)){
                    resolvedValue = helper.replacePlaceholders(value, System::getenv);
                }
                props.setProperty(name, resolvedValue);
            }
            String mdDirStr = props.getProperty("com.ssg.mddir");
            String htmlDirStr = props.getProperty("com.ssg.htmldir");
            String picoThemeStr = props.getProperty("com.ssg.pico.theme");
            String siteTitleStr = props.getProperty("com.ssg.site.title");
            String reloadAllFilesStr = props.getProperty("com.ssg.reload");
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
                throw new Exception("com.ssg.htmldir not set in application.properties");
            }
            System.out.println("INFO: picoTheme:" + picoTheme);
            picoTheme = picoThemeStr.toLowerCase();
            if (siteTitleStr == null) {
                throw new Exception("com.ssg.site.title not set in application.properties");
            }
            System.out.println("INFO: siteTheme: " + siteTitleStr);
            siteTitle = siteTitleStr;
            if (reloadAllFilesStr == null) {
                throw new Exception("com.ssg.reload not set in application.properties");
            }
            System.out.println("INFO: reload:" + reloadAllFilesStr);
            reloadAllFiles = reloadAllFilesStr.equals("true");
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
