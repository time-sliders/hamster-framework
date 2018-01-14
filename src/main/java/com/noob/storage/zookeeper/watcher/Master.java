package com.noob.storage.zookeeper.watcher;

import com.noob.storage.common.MillisecondInt;
import com.noob.storage.zookeeper.sync.TryRunAsMaster;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * zookeeper监视点
 *
 * @author luyun
 * @version 1.0
 * @since 2018.01.11
 */
public class Master implements Watcher {

    private ZooKeeper zooKeeper;

    private Master() {
    }

    private void startZookeeper() throws IOException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", MillisecondInt.TEN_MINUS, this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    private void stopZookeeper() throws InterruptedException {
        zooKeeper.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Master m = new Master();
        m.startZookeeper();

        try {
            boolean isSuccess = TryRunAsMaster.tryMaster(m.zooKeeper, "/master", "127.0.0.1");
            System.out.println("try result:" + isSuccess);
        } catch (KeeperException e) {
            e.printStackTrace();
        }

        m.stopZookeeper();


    }


}
