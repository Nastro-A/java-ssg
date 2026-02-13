package com.ssg.ssg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import static com.ssg.ssg.utils.DirectoryUtils.ifDirNotExistentCreateElseOk;
import static com.ssg.ssg.utils.IndexHtml.createIndex;

@SpringBootApplication
public class SsgApplication {
    private static Path mdDir;
    private static Path htmlDir;
    public static String picoTheme;
    public static String siteTitle;
    static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream input = SsgApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
                String mdDirStr = props.getProperty("com.ssg.mddir");
                String htmlDirStr = props.getProperty("com.ssg.htmldir");
                String picoThemeStr = props.getProperty("com.ssg.pico.theme");
                String siteTitleStr = props.getProperty("com.ssg.site.title");
                if (mdDirStr == null) {
                    throw new Exception("com.ssg.mddir not set in application.properties");
                }
                mdDir = Path.of(mdDirStr);
                if (htmlDirStr == null) {
                    throw new Exception("com.ssg.htmldir not set in application.properties");
                }
                htmlDir = Path.of(htmlDirStr);
                if (picoThemeStr == null) {
                    throw new Exception("com.ssg.htmldir not set in application.properties");
                }
                picoTheme = picoThemeStr.toLowerCase();
                siteTitle = siteTitleStr;
            }
        } catch (IOException e) {
            System.out.println("application.properties file not exists");
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        if (ifDirNotExistentCreateElseOk(mdDir) && ifDirNotExistentCreateElseOk(htmlDir)) {
            createIndex(htmlDir, siteTitle, picoTheme);
            SpringApplication.run(SsgApplication.class, args);
        }
    }

}
