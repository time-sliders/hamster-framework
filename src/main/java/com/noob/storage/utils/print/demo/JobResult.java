package com.noob.storage.utils.print.demo;

import com.noob.storage.common.Millisecond;
import com.noob.storage.utils.IPUtils;
import com.noob.storage.utils.print.TableLocation;
import org.springframework.util.StopWatch;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.11.04
 */
public class JobResult {

    protected StopWatch sw;

    @TableLocation(propertyName = "主机IP", fieldTitle = "全局信息", row = -1, col = 1)
    private String hostIp;

    @TableLocation(propertyName = "任务耗时", row = -1, col = 2)
    private String cost;

    public JobResult() {
        hostIp = IPUtils.getLocalIp();
        sw = new StopWatch();
        sw.start();
    }

    public void finish() {
        sw.stop();
        cost = sw.getTotalTimeMillis() / Millisecond.ONE_SECONDS +"秒";
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
