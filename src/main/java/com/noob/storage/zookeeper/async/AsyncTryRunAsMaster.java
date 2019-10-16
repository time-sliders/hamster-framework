package com.noob.storage.zookeeper.async;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 客户端尝试通过建立节点的方式，成为Master
 *
 * @param <C> 应用上下文Context
 * @author luyun
 * @version 1.0
 * @since 2018.01.14
 */
public abstract class AsyncTryRunAsMaster<C> {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTryRunAsMaster.class);

    /**
     * zooKeeper客户端链接
     */
    protected ZooKeeper zooKeeper;

    /**
     * 需要创建的路径，如/myth/master。
     */
    protected String masterPath;

    /**
     * 需要存储的数据，一般为当前 Server 的唯一标识，如IP等。
     */
    protected String masterPathData;

    /**
     * 应用上下文
     */
    protected C ctx;

    public AsyncTryRunAsMaster(ZooKeeper zooKeeper, String masterPath, String masterPathData, C ctx) {
        this.zooKeeper = zooKeeper;
        this.masterPath = masterPath;
        this.masterPathData = masterPathData;
        this.ctx = ctx;
    }

    /**
     * 客户端尝试通过建立节点的方式，成为Master
     */
    public void tryMaster() {
        byte[] pathDataBytes = null;
        if (StringUtils.isNotBlank(masterPathData)) {
            pathDataBytes = masterPathData.getBytes(StandardCharsets.UTF_8);
        }
        zooKeeper.create(masterPath, pathDataBytes, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, createMasterCallback, ctx);
    }

    private AsyncCallback.StringCallback createMasterCallback = (rc, path, ctx, name) -> {
        switch (KeeperException.Code.get(rc)) {
            case OK:
                /*
                 * 情况1：成功当选群首
                 */
                masterCallback();
                break;

            case NODEEXISTS:
            case CONNECTIONLOSS:
                /*
                 * 情况2：结果未知，反查确认
                 */
                checkMaster();
                break;

            default:
                /*
                 * 情况3：当选失败
                 */
        }
    };


    private void checkMaster() {
        zooKeeper.getData(masterPath, false, dataCallBack, ctx);
    }

    private AsyncCallback.DataCallback dataCallBack = (rc, path, ctx, data, stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                logger.info("ZooKeeper getNodeData occur KeeperException.ConnectionLossException>Thread_Sleep_ONE_SECONDS to CONTINUE.....");
                checkMaster();
                break;

            case NONODE:
                tryMaster();
                break;

            default:
        }
    };

    /**
     * 群首回调
     */
    protected abstract void masterCallback();


}
