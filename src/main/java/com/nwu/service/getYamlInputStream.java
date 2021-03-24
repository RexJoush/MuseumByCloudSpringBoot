package com.nwu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class getYamlInputStream {
    public static InputStream byPath(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream yamlInputStream = fileInputStream;
        return yamlInputStream;
    }
}
