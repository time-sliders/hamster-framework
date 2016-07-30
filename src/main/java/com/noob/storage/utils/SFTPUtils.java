package com.noob.storage.utils;

import com.jcraft.jsch.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;
import java.util.Vector;

/**
 * SFTP工具
 */
public class SFTPUtils {

    /**
     * sftp免密码登录
     */
    public static ChannelSftp getConnectionWithoutPwd(String server,
                                                      int port,
                                                      String username,
                                                      String privateKey,
                                                      String passphrase) throws Exception {
        Channel channel;
        JSch jsch = new JSch();
        if (StringUtils.isBlank(passphrase)) {
            jsch.addIdentity(privateKey);
        } else {
            jsch.addIdentity(privateKey, passphrase);
        }

        Session session = jsch.getSession(username, server, port);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setServerAliveInterval(90000);
        session.connect(30000);

        channel = session.openChannel("sftp");
        channel.connect(1000);
        return (ChannelSftp) channel;
    }

    /**
     * 密码登录
     */
    public static ChannelSftp getConnection(String server,
                                            int port,
                                            String username,
                                            String password) throws Exception {
        ChannelSftp sftp;
        Session session;
        Channel channel;
        JSch jsch = new JSch();

        if (port <= 0) {
            //连接服务器，采用默认端口
            session = jsch.getSession(username, server);
        } else {
            //采用指定的端口连接服务器
            session = jsch.getSession(username, server, port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(password);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);


        //创建sftp通信通道
        channel = session.openChannel("sftp");
        channel.connect(1000);
        sftp = (ChannelSftp) channel;

        return sftp;
    }

    /**
     * 验证在 path目录下是否存在 fileName 文件
     *
     * @param channelSftp sftp连接
     * @param path        查找的路径
     * @param fileName    查找的文件名
     */
    public static boolean isFileExists(ChannelSftp channelSftp,
                                       String path,
                                       String fileName) throws Exception {

        Vector vector = channelSftp.ls(path);

        if (CollectionUtils.isEmpty(vector)) {
            return false;
        }

        for (Object aVector : vector) {
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) aVector;
            if (lsEntry.getFilename().equals(fileName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 静默关闭连接
     */
    public static void closeQuietly(ChannelSftp sftp) {
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException ignore) {
            }
            sftp.disconnect();
        }
    }
}
