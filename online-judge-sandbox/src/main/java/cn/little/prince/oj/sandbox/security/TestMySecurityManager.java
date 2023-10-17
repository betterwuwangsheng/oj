package cn.little.prince.oj.sandbox.security;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestMySecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());

        // 读文件
        List<String> strings = FileUtil.readLines(new File("C:\\Users\\349807102\\Desktop\\oj\\online-judge-sandbox\\src\\main\\resources\\application.yml"), StandardCharsets.UTF_8);
        System.out.println(strings);

    }
}
