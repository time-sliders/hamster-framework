package com.noob.storage.component.seq;

import com.noob.storage.utils.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 时间戳序列生成器
 *
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public class TSSGenerator {

    private static String lastTS = null;
    private static int increment;
    private static final int incINIT = 1;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final SimpleDateFormat fmt = new SimpleDateFormat(DateUtil.FMR_MILL_NO_LINE);

    /**
     * 生成15位时间戳 + 同一毫秒内3位递增序列值
     */
    public static String getTSSequence() {
        String currTS;
        lock.lock();
        try {
            currTS = fmt.format(new Date());
            if (lastTS == null || !lastTS.equals(currTS)) {
                lastTS = currTS;
                increment = incINIT;
            } else {
                increment = increment + 1;
            }
        } finally {
            lock.unlock();
        }
        return currTS + StringUtils.leftPad(String.valueOf(increment), 3, '0');
    }

}
