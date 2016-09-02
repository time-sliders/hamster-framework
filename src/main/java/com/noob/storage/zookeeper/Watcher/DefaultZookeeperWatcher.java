package com.noob.storage.zookeeper.watcher;

import com.noob.storage.pattern.adapter.AbstractAdapter;
import com.noob.storage.zookeeper.watcher.handler.ZookeeperEventHandler;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

/**
 * zookeeper 的事件回调接口
 *
 * @author luyun
 */
@Component
public class DefaultZookeeperWatcher
        extends AbstractAdapter<Watcher.Event.KeeperState, ZookeeperEventHandler>
        implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        ZookeeperEventHandler handler = getExecutor(event.getState());
        if (handler != null) {
            handler.execute(event);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerExecutor(Event.KeeperState.Expired, "expiredEventHandler");
        registerExecutor(Event.KeeperState.SyncConnected, "syncConnectedEventHandler");
        registerExecutor(Event.KeeperState.Disconnected, "disconnectedEventHandler");
        registerExecutor(Event.KeeperState.AuthFailed, "authFailedEventHandler");
        super.afterPropertiesSet();
    }
}
