package com.noob.storage.zookeeper;

import com.noob.storage.zookeeper.watcher.DefaultZookeeperWatcher;
import org.apache.zookeeper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author luyun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-context.xml")
public class TestZookeeper {

    @Autowired
    private DefaultZookeeperWatcher defaultZookeeperWatcher;

    @Test
    public void testZoo() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1", 2000, defaultZookeeperWatcher);
        String path = "/root";
        zooKeeper.create(path, "zhwwashere".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        while(true){
            System.out.println(new String(zooKeeper.getData(path, false, null)));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                break;
            }
        }
        zooKeeper.close();
    }

}
