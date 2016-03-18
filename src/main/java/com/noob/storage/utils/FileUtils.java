package com.noob.storage.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件操作工具类
 */
public class FileUtils {

    public static final String WINDOW_FILE_PROTOCOL = "file://";

    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * 获取路径下所有的文件全路径，不包含空的文件夹
     */
    public static List<String> getAllFiles(String path) {
        List<String> files = null;
        if (StringUtils.isNotBlank(path)) {
            files = new LinkedList<String>();
            File pathfile = new File(path);
            File[] pathfiles = pathfile.listFiles();
            for (int i = 0; i < pathfiles.length; i++) {
                if (pathfiles[i].isFile()) {
                    files.add(pathfiles[i].getAbsolutePath());
                } else {
                    files.addAll(getAllFiles(pathfiles[i].getAbsolutePath()));
                }
            }
        }
        return files;
    }

    /**
     * 复制文件，如果目标文件已经存在，那么删除文件重新创建
     */
    public static void copyFile(String src, String dest) {
        FileInputStream srcFis = null;
        FileOutputStream destFos = null;
        try {
            byte[] buffer = new byte[1024 * 1024];
            srcFis = new FileInputStream(new File(src));
            File destFile = new File(dest);
            if (destFile.exists()) {
                destFile.delete();
            }
            destFile.createNewFile();
            destFos = new FileOutputStream(destFile);
            int count = -1;
            while ((count = srcFis.read(buffer)) != -1) {
                destFos.write(buffer, 0, count);
            }
        } catch (Exception e) {
            logger.error("文件复制失败：" + e.getMessage());
        } finally {
            if (srcFis != null) {
                try {
                    srcFis.close();
                } catch (IOException e) {
                }
            }
            if (destFos != null) {
                try {
                    destFos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 递归删除指定目录的所有文件和文件夹
     *
     * @param path 被删除的目录
     */
    public static void deleteFileByPath(String path) {
        File file = new File(path);
        /** 如果传入的路径只是一个文件，那么直接删除文件 */
        if (file.isFile()) {
            file.delete();
            /** 如果传入的是一个目录，那么递归删除该目录下所有文件 */
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFileByPath(files[i].getAbsolutePath());
            }
            file.delete();
        }
    }

    /**
     * 获取指定类的绝对路径
     *
     * @param clazz 查询的类的路径</br>
     *              com.sunyard.frame.common.FileUtils--></br>
     *              /E:/MyEclipse2014_workSpace/finance/WebRoot/WEB-INF/classes/com/sunyard/frame/common/FileUtils.class
     * @author zhw
     * @since 2014年1月26日|下午3:28:56
     */
    public static String getRealPathName(Class<?> clazz) {
        String className = clazz.getName();
        int pos = className.lastIndexOf('.') + 1;
        URL url = clazz.getResource(className.substring(pos) + ".class");
        if (url != null) {
            return url.getPath();
        }
        return null;
    }

    public static void createFileWithPath(String path) throws IOException {
        if (StringUtils.isBlank(path)) {
            return;
        }
        path = path.replaceAll("[\\\\]", "/");
        String fileDir = path.substring(0, path.lastIndexOf("/") + 1);
        File dirFile = new File(fileDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

}
