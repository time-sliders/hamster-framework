package com.noob.storage.io;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class FileCharsetConvertUtil {

    public static void main(String[] args) throws IOException {
        convert("1", "GB2312");
    }

    public static void convert(String originalFileName, String charset) throws IOException {
        String workPath = "/Users/zhangwei/Downloads/1/";
        File originalFile = new File(workPath + originalFileName);
        InputStream is = new FileInputStream(originalFile);
        File targetFile = new File(workPath + "/2/" + charset + "-" + originalFileName);
        if (targetFile.exists() && !targetFile.delete()) {
            throw new RuntimeException("cannot delete old file:" + targetFile.getAbsolutePath());
        }
        if (!targetFile.createNewFile()) {
            throw new RuntimeException("cannot create new file:" + targetFile.getAbsolutePath());
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(targetFile));
        IOUtils.copy(is, writer, charset);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(writer);
        System.out.println(targetFile.getAbsoluteFile() + " created success");
    }
}
