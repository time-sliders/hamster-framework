package com.noob.storage.model;

import com.noob.storage.common.Millisecond;
import com.noob.storage.utils.DateUtil;
import com.noob.storage.utils.IPUtils;
import com.noob.storage.utils.print.TableLocation;
import org.springframework.util.StopWatch;

import java.util.Date;

/**
 * @author luyun
 * @version 1.0
 * @since 2017.11.04
 */
public class JobResult {

    protected StopWatch sw;

    @TableLocation(propertyName = "主机IP", fieldTitle = "全局信息", row = -99, col = 1)
    private String hostIp;

    @TableLocation(propertyName = "任务耗时", row = -99, col = 2)
    private String cost;

    @TableLocation(propertyName = "开始时间", row = -98, col = 1)
    private String startTime;

    @TableLocation(propertyName = "结束时间", row = -98, col = 2)
    private String endTime;

    public JobResult() {
        hostIp = IPUtils.getLocalIp();
        startTime = DateUtil.format(new Date(), DateUtil.DEFAULT_FORMAT);
        sw = new StopWatch();
        sw.start();
    }

    public void finish() {
        sw.stop();
        endTime = DateUtil.format(new Date(), DateUtil.DEFAULT_FORMAT);
        cost = sw.getTotalTimeMillis() / Millisecond.ONE_SECONDS + "秒";
    }
}
