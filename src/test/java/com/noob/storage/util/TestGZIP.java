package com.noob.storage.util;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;

/**
 * 压缩文件与解压文件
 *
 * @author luyun
 * @since 2016.06.16
 */
public class TestGZIP {

    @Test
    public void testCreateZIPFile() throws Exception {

        final OutputStream out = new FileOutputStream("/Users/zhangwei/test/zip/all.zip");

        ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);

        File file1 = new File("/Users/zhangwei/test/zip/1");
        File file2 = new File("/Users/zhangwei/test/zip/2");

        os.putArchiveEntry(new ZipArchiveEntry("1"));
        InputStream is1 = new FileInputStream(file1);
        IOUtils.copy(is1, os);
        os.closeArchiveEntry();

        os.putArchiveEntry(new ZipArchiveEntry("2"));
        InputStream is2 = new FileInputStream(file2);
        IOUtils.copy(is2, os);
        os.closeArchiveEntry();

        os.finish();

        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is1);
        IOUtils.closeQuietly(is2);
    }

    @Test
    public void testUnpackingZIPFile() throws Exception {

        File input = new File("/Users/zhangwei/test/zip/all.zip");
        final InputStream is = new FileInputStream(input);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
        ZipArchiveEntry entry;
        while ((entry = (ZipArchiveEntry) in.getNextEntry()) != null) {
            OutputStream out = new FileOutputStream(new File("/Users/zhangwei/test/zip/", entry.getName()));
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(out);
        }

        IOUtils.closeQuietly(is);

    }

    /**
     * 看上去压缩率比zip要高
     *
     * @throws Exception
     */
    @Test
    public void testCompressFile() throws Exception {

        final OutputStream out = new FileOutputStream("/Users/zhangwei/test/zip/compress.bzip2");
        final InputStream is = new FileInputStream("/Users/zhangwei/test/zip/1");

        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream("bzip2", out);

        IOUtils.copy(is, cos);

        IOUtils.closeQuietly(cos);
        IOUtils.closeQuietly(is);
    }

    @Test
    public void testDecompressingFile() throws Exception {

        final InputStream is = new FileInputStream("/Users/zhangwei/test/zip/compress.bzip2");
        final OutputStream os = new FileOutputStream("/Users/zhangwei/test/zip/3");

        CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream("bzip2", is);

        IOUtils.copy(in, os);

        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);
    }

}
