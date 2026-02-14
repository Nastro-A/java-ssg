package com.ssg.ssg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class WebServer {
    @Autowired
    private Environment env;

    @GetMapping({ "/", "/index.html"})
    private String getIndex(){
        try{
           return Files.readString(Path.of(env.getProperty("com.ssg.htmldir") + "/index.html"));
        } catch (IOException e) {
            System.out.println("Error: File index.html not accessible!");
            System.exit(1);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{page}")
    private String getPage(@PathVariable String page){
        try {
            return Files.readString(Path.of(env.getProperty("com.ssg.htmldir") + "/" +  page));
        } catch (IOException e) {
            System.out.println("Error: File " + page + " not accessible!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
