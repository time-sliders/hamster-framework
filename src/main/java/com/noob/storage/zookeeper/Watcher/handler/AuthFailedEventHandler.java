package com.noob.storage.zookeeper.watcher.handler;

import org.apache.zookeeper.WatchedEvent;
import org.springframework.stereotype.Component;

/**
 * @author luyun
 */
@Component
public class AuthFailedEventHandler implements ZookeeperEventHandler {

    @Override
    public void execute(WatchedEvent event) {
        System.out.println("AuthFailed");
    }
}
