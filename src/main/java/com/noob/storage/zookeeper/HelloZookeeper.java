package com.noob.storage.zookeeper;

import com.noob.storage.common.Seconds;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author luyun
 * @version 1.0
 * @since 2018.01.11
 */
public class HelloZookeeper {


    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", Seconds.SIX_SECONDS, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType());
            }
        });
    }

}
