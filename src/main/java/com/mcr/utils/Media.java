package com.mcr.utils;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Media {
    public static String mediaBasePath;
    public static String nullPng = "null.png";
    public static Boolean validPathFile(String path) {
        File file = new File(mediaBasePath + path);
        return file.exists();
    }

}
