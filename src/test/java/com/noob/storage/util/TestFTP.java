package com.noob.storage.util;

import com.noob.storage.utils.FTPClientUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;

/**
 * @author luyun
 * @since 2016.06.15
 */
public class TestFTP {

    @Test
    public void testFtpUpload() {
        FTPClient ftpClient = null;
        try {
            ftpClient = FTPClientUtil.connect("192.168.1.101", 21, "test", "123456", "/tmp/");
            FTPClientUtil.storeFile(new File("/Users/zhangwei/test/ftpTest"),ftpClient);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FTPClientUtil.closeConnection(ftpClient);
        }
    }

    @Test
    public void testFtpDownload() {
        FTPClient ftpClient = null;
        try {
            ftpClient = FTPClientUtil.connect("192.168.1.101", 21, "test", "123456", "/tmp/");
            FTPClientUtil.downLoad("/tmp/ftpTest","/Users/zhangwei/test/ftpTest",ftpClient);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FTPClientUtil.closeConnection(ftpClient);
        }
    }

    @Test
    public void testFtpDelete() {
        FTPClient ftpClient = null;
        try {
            ftpClient = FTPClientUtil.connect("192.168.1.101", 21, "test", "123456", "/tmp/");
            ftpClient.deleteFile("/tmp/ftpTest");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FTPClientUtil.closeConnection(ftpClient);
        }
    }


}
