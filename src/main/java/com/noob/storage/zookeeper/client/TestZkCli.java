package com.noob.storage.zookeeper.client;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * @author luyun
 * @version ZERO 2.0
 * @since 2017.08.31
 */
public class TestZkCli {

    public static void main(String[] args) {
        try {
            String path = "/zhw";
            ZkClient zkClient = new ZkClient("127.0.0.1:2181");
            zkClient.create(path, "zhw_was_here", CreateMode.PERSISTENT);
            System.out.println((String) zkClient.readData(path));
            zkClient.delete(path);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
