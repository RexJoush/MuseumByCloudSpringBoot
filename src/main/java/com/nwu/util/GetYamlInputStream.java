package com.nwu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GetYamlInputStream {
    public static InputStream byPath(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream yamlInputStream = fileInputStream;
        return yamlInputStream;
    }
}
