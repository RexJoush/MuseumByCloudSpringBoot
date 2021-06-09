package com.nwu.util;

public class DealYamlStringFromFront {
    public static String dealYamlStringFromFront(String yaml){
        // 去掉传输头尾
        String s1 = yaml.replaceFirst("[{]\"[A-Za-z]+\":\"", "");
        String substring = s1.substring(0, s1.length() - 2);

        // 将 \" 转换为 " 将 \\ 转换为 \
        // 即，去掉前后端传值时自动添加的后端不会用到的转义字符
        yaml = substring.replaceAll("\\\\\"", "\"").replaceAll("\\\\\\\\","\\\\").replaceAll("\\\\n", "\r\n");
        return yaml;
    }
}
