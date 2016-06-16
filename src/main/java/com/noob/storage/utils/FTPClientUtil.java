package com.noob.storage.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * FTP工具
 *
 * @author luyun
 * @since 2016.06.15
 */
public class FTPClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPClientUtil.class);

    /**
     * 连接文件服务器
     *
     * @param addr     文件服务器地址
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     */
    public static FTPClient connect(String addr,
                                    int port,
                                    String username,
                                    String password) throws IOException {

        logger.info("connecting ftp server >>> addr:%s, port:%s, user:%s",
                new Object[]{addr, port, username});

        FTPClient ftpClient = new FTPClient();
        // 连接
        ftpClient.connect(addr, port);
        // 登录
        if (!ftpClient.login(username, password)) {
            throw new RuntimeException("login ftp server fail, please check username & password !");
        }

        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        // 判断文件服务器是否可用
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            closeConnection(ftpClient);
            throw new RuntimeException("ftpClient is not positive completion!");
        }
        return ftpClient;
    }

    /**
     * 连接文件服务器
     *
     * @param addr             文件服务器地址
     * @param port             端口
     * @param username         用户名
     * @param password         密码
     * @param workingDirectory 目标连接工作目录
     * @throws Exception
     */
    public static FTPClient connect(String addr,
                                    int port,
                                    String username,
                                    String password,
                                    String workingDirectory) throws Exception {

        FTPClient ftpClient = connect(addr, port, username, password);
        changeWorkingDirectory(workingDirectory, ftpClient);
        return ftpClient;
    }

    /**
     * 关闭连接，使用完连接之后，一定要关闭连接，否则服务器会抛出 Connection reset by peer的错误
     */
    public static void closeConnection(FTPClient ftpClient) {

        if (ftpClient == null || !ftpClient.isConnected()) {
            return;
        }

        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            logger.warn("ftp client close fail!", e);
        }
    }

    /**
     * 切换工作目录
     *
     * @param directory 目标工作目录
     */
    public static boolean changeWorkingDirectory(String directory, FTPClient ftpClient) throws IOException {
        return ftpClient.changeWorkingDirectory(directory) ||
                (ftpClient.makeDirectory(directory) && ftpClient.changeWorkingDirectory(directory));

    }

    /**
     * 存储文件
     */
    public static void storeFile(File file, FTPClient ftpClient) throws Exception {

        if (file == null) {
            return;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            if (!ftpClient.storeFile(file.getName(), fis)) {
                throw new RuntimeException("ftp store file fail!");
            }
        } finally {
            IOUtils.closeQuietly(fis);
        }

        logger.info("ftp upload file : " + file.getName());
    }

    /**
     * 下载文件到指定目录
     *
     * @param ftpFile 文件服务器上的文件地址
     * @param dstFile 输出文件的路径和名称
     * @throws Exception
     */
    public static void downLoad(String ftpFile, String dstFile, FTPClient ftpClient) throws Exception {

        if (StringUtils.isBlank(ftpFile) || StringUtils.isBlank(dstFile)) {
            return;
        }
        File file = new File(dstFile);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (!ftpClient.retrieveFile(ftpFile, fos)) {
                throw new RuntimeException("ftp file download fail!");
            }
        } finally {
            IOUtils.closeQuietly(fos);
        }

        logger.info("ftp download file : " + ftpFile + " , dstFile : " + dstFile);
    }

}
