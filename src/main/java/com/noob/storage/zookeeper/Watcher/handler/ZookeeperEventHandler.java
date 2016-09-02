package com.noob.storage.zookeeper.watcher.handler;

import org.apache.zookeeper.WatchedEvent;

/**
 * zookeeper事件处理器
 *
 * @author luyun
 */
public interface ZookeeperEventHandler {

    void execute(WatchedEvent event);

}
