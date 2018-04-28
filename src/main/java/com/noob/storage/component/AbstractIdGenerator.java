package com.noob.storage.component;

import java.lang.management.ManagementFactory;

/**
 * 增强的雪花算法ID生成器
 * thread-safe
 *
 * @author LuYun
 * @since 2018.04.28
 */
public abstract class AbstractIdGenerator {

    /**
     * 进程PID
     * 默认最大值 32768 = 2^15
     * /proc/sys/kernel/pid_max
     */
    private static final Long PID = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    //最大业务分类数目 128
    private static final long MAX_BIZ_IDX = 1 << 7;
    //最大机器个数 128
    private static final long MAX_MACHINE_INDEX = 1 << 7;
    //每毫秒内最多生成Id的个数 1024
    private static final long MAX_MILLS_IDX = 1 << 10;
    private final TimeStampSequence tss = new TimeStampSequence();

    /**
     * |<-------- 42位TimeStamp -------->|
     * |<---10位systemCode--->|
     *
     * 42+15+7+7+10
     */
    public long getId() {

        /*
         * 25
         * 201810
         * ddHHmmss
         * 2199023255552
         * 1524901523913
         * 2^42 / (1000 * 60 * 60 * 24 * 365 ) + 1970 ~ 2109 年
         */
        Long currentTimeMillis = System.currentTimeMillis();

        long bizIdx = getBusinessIndex();
        if (bizIdx < 0 || bizIdx > MAX_BIZ_IDX) {
            throw new RuntimeException("biz index out of range : " + bizIdx);
        }

        long machineIdx = getMachineIndex();
        if (machineIdx < 0 || machineIdx > MAX_MACHINE_INDEX) {
            throw new RuntimeException("biz index out of range : " + bizIdx);
        }

        /*
         * 3字节
         * 12位毫秒内自增索引
         */
        long tssIdx = tss.getTss(currentTimeMillis);

        return 0L;
    }

    private long getTssIdx(Long currentTimeMillis) {
        return 0;
    }

    class TimeStampSequence {
        private long currentTimeMills = 0;
        private long idx = 0;

        synchronized long getTss(long currentTimeMills) {
            if (this.currentTimeMills != currentTimeMills) {
                this.currentTimeMills = currentTimeMills;
                idx = 0;
            }
            long value = idx++;
            if (value > MAX_MILLS_IDX) {
                throw new RuntimeException("tss value out of range : " + idx);
            }
            return value;
        }
    }

    /**
     * 获取10位业务代码Code
     *
     * @return 0 <= bizCode < (2^10=1024)
     */
    protected abstract long getBusinessIndex();

    /**
     * 获取机器代码
     *
     * @return 0 <= MachineCode < (2^10=1024)
     */
    protected abstract long getMachineIndex();

}
