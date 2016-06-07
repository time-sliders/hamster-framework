package com.noob.storage.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 对字节数组进行GZIP压缩和解压
 */
public class GzipUtils {

    /** 默认编码 */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 压缩GZip
     * 
     * @param data 需要压缩的字节数组
     * @return 压缩后的字节数组
     */
    public static byte[] gZip(String data) throws IOException {
        if (null == data || 0 == data.length()) {
            return new byte[0];
        }
        byte[] b = null;
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gzip = null;
        try {
            bos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(bos);
            gzip.write(data.getBytes(DEFAULT_CHARSET));
            gzip.finish();
            b = bos.toByteArray();
        } finally {
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(bos);
        }
        return b;
    }

    /**
     * 解压GZip
     * 
     * @param data 需要解压的字节数组
     * @return 解压后的字节数组
     */
    public static String unGZip(byte[] data) throws IOException {
        if (null == data || 0 == data.length) {
            return "";
        }
        byte[] b = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num;
            baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();

        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(bis);
        }
        return new String(b, DEFAULT_CHARSET);
    }

    /**
     * 压缩GZip
     * 
     * @param data 需要压缩的字节数组
     * @return 压缩后的字节数组
     */
    public static byte[] gZip(byte[] data) {
        byte[] b = null;
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gzip = null;
        try {
            bos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            b = bos.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(bos);
        }
        return b;
    }

    /**
     * 解压GZip
     * 
     * @param data 需要解压的字节数组
     * @return 解压后的字节数组
     */
    public static byte[] unGZipToBytes(byte[] data) {
        byte[] b = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num;
            baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();

        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(gzip);
            IOUtils.closeQuietly(bis);
        }
        return b;
    }
    
    public static void main(String[] args) throws IOException {
        byte[] butyArray = GzipUtils.gZip("helloworld");
        String result = new String(butyArray);
        System.out.println(result);
        
        String newResult = GzipUtils.unGZip(butyArray);
        System.out.println(newResult);
    }
}
