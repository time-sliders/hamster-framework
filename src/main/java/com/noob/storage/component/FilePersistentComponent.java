package com.noob.storage.component;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 数据文件持久组件<br/>
 * 负责将指定的数据写入到文件中
 *
 * @author luyun
 */
public class FilePersistentComponent {

    private static final Logger logger = LoggerFactory.getLogger(FilePersistentComponent.class);

    /**
     * 临时文件后缀
     */
    private static final String TMP_FILE_SUFFIX = ".tmp";

    public static void writeWithTmp(String filePath, String fileName, String content) throws IOException {

        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(filePath) || StringUtils.isBlank(content)) {
            return;
        }

        // 确认&创建公告文件目录
        File dir = new File(filePath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("无法创建文件[" + dir.getAbsolutePath() + "],请确认路径权限,inode等情况");
        }

        // 确认文件不存在
        File file = new File(filePath + File.separatorChar + fileName);
        if (file.exists() && file.isFile()) {
            return;
        }

        // 创建临时文件
        writeStringToFile(filePath, fileName + TMP_FILE_SUFFIX, content);

        // 将临时文件名修改为正式文件
        File tmpFile = new File(filePath + File.separatorChar + fileName + TMP_FILE_SUFFIX);
        FileUtils.moveFile(tmpFile, file);

    }


    public static void writeStringToFile(String filePath, String fileName, String content) throws IOException {

        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(filePath) || StringUtils.isBlank(content)) {
            return;
        }

        // 确认&创建文件目录
        File dir = new File(filePath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("无法创建目录[" + dir.getAbsolutePath() + "],请确认路径权限");
        }

        // 确认文件不存在
        File file = new File(filePath + File.separatorChar + fileName);
        if (file.exists() && file.isFile() && !file.delete()) {
            return;// 已经有其他线程在操作
        }

        // 创建新文件
        if (!file.createNewFile()) {
            throw new IOException("无法创建文件[" + file.getAbsolutePath() + "],请确认路径权限,inode等情况");
        }

        // 将数据写入到临时文件
        try {
            FileUtils.writeStringToFile(file, content);
        } catch (IOException e) {
            throw new IOException("数据写入文件失败[" + file.getAbsolutePath() + "],请检查磁盘容量情况");
        }

    }

}
