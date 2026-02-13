package com.ssg.ssg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import static com.ssg.ssg.utils.DirectoryUtils.ifDirNotExistentCreateElseOk;

@SpringBootApplication
public class SsgApplication {
    private static Path mdDir;
    private static Path htmlDir;
    static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream input = SsgApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
                String mdDirStr = props.getProperty("com.ssg.mddir");
                String htmlDirStr = props.getProperty("com.ssg.htmldir");
                if (mdDirStr == null) {
                    throw new Exception("com.ssg.mddir not set in application.properties");
                }
                mdDir = Path.of(mdDirStr);
                if (htmlDirStr == null) {
                    throw new Exception("com.ssg.htmldir not set in application.properties");
                }
                htmlDir = Path.of(htmlDirStr);
            }
        } catch (IOException e) {
            System.out.println("application.properties file not exists");
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        if (ifDirNotExistentCreateElseOk(mdDir) && ifDirNotExistentCreateElseOk(htmlDir)) {
            SpringApplication.run(SsgApplication.class, args);
        }
    }

}
