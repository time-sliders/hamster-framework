package com.noob.storage.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动程序
 *
 * @author luyun
 * @version 1.0
 * @since 2018.01.14
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private ZooKeeper zooKeeper;

    public static void main(String[] args) {
        new Bootstrap().bootstrap();
    }

    /**
     * 启动程序
     */
    public void bootstrap() {
        /*
         * 1.创建系统所需的几个永久节点
         */
        byte[] EMPTY_BYTES = new byte[0];
        createZooKeeperPath("/myth", EMPTY_BYTES);
        //myth主控服务器节点
        createZooKeeperPath("/myth/master", EMPTY_BYTES);
        //各个系统的子节点
        createZooKeeperPath("/myth/system", EMPTY_BYTES);
    }

    /**
     * 异步创建ZooKeeper目录
     *
     * @param path ZooKeeper Node节点
     * @param data Node中存储的数据
     */
    private void createZooKeeperPath(String path, byte[] data) {
        zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createZooKeeperPathCallback, data);
    }

    private AsyncCallback.StringCallback createZooKeeperPathCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createZooKeeperPath(path, (byte[]) ctx);
                    break;

                case NODEEXISTS:
                    logger.info(path + " already registered");
                    break;

                case OK:
                    logger.info(path + " created");
                    break;

                default:
                    logger.info("Something went wrong:" + KeeperException.create(rc, path));
            }
        }
    };


}
