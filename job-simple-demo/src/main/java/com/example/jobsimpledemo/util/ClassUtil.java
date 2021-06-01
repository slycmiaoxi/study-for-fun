package com.example.jobsimpledemo.util;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class ClassUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);


    /**
     * 获取指定包名下所有类
     * @param packageName (xxx/xxx/xxx)
     * @return
     */
    public static List<String> getClassList(String packageName) {
        List<String> clsList = new ArrayList<>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (null != url) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", "");
                        addList(clsList, packagePath, packageName);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("获取指定包下所有类失败！");
        }
        return clsList;
    }

    /**
     * 根据类名获取job类
     * @param classname
     * @return
     * @throws Exception
     */
    public static Class<? extends Job> getJobClass(String classname) throws ClassNotFoundException {
        Class<?> cls = Class.forName(classname);
        return (Class<? extends Job>) cls;
    }

    private static void addList(List<String> clsList, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
            }
        });
        for (File file : files) {
            //获取文件名
            String fileName = file.getName();
            //判断是否为文件
            if (file.isFile()) {
                //截取获取类名
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                String classPath = packageName.replaceAll("/", ".") + "." + className;
                doAddList(clsList, classPath);
            } else {
                //如果当前不是文件 获取当前包名
                String subPackagePth = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePth = packagePath + "/" + subPackagePth;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(subPackageName)) {
                    subPackageName = packageName + "/" + subPackageName;
                }
                addList(clsList,subPackagePth,subPackageName);
            }
        }
    }

    private static void doAddList(List<String> clsList, String classPath) {
        try {
            Class<?> cls = Class.forName(classPath);
            if (Job.class.isAssignableFrom(cls)) {
                clsList.add(cls.getName());
            }
        } catch (ClassNotFoundException e) {
            logger.error("无法找到类" + classPath);
        }
    }
}
