package com.noob.storage.common.clazz;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 从指定的系统目录下加载类的ClassLoader
 */
public class DirClassLoader extends ClassLoader {

    private static Logger log = Logger.getLogger(DirClassLoader.class);

    public static final String classDir = System.getProperty("dynamicClassDir");

    public Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz;
        if ((clazz = findLoadedClass(name)) != null) {
            return clazz;
        }
        String classFilePath;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            classFilePath = classDir + name.replace('.', File.separatorChar) + ".class";
            File file = new File(classFilePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            IOUtils.copy(fis, bos);
            byte[] classBytes = bos.toByteArray();
            clazz = defineClass(name, classBytes, 0, classBytes.length);
            resolveClass(clazz);
            return clazz;
        } catch (Throwable t) {
            log.error(t);
            throw new ClassNotFoundException(name, t);
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(bos);
        }
    }
}
