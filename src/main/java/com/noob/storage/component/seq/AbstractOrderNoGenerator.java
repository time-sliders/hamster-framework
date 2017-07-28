package com.noob.storage.component.seq;

import com.noob.storage.utils.IPUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * 抽象的订单号生成器
 *
 * @author luyun
 * @version 2017.7.31 TB to HQB
 * @since 2017.07.27
 */
public class AbstractOrderNoGenerator {

    private static final Random random = new Random();
    private static final String BANK_UP_2 = "00";
    private static final String DEFAULT_IP = "000000";

    /**
     * 生成包含业务类型的32位订单id
     *
     * @param userId  用户ID,数值
     * @param bizCode 业务代码,2位,建议数值
     */
    public static String buildOrderNoWithBizCode(String userId, String bizCode) {
        return build(userId, StringUtils.leftPad(bizCode, 2, '0'));
    }

    /**
     * 生成32位订单id
     *
     * @param userId  用户ID,数值
     * @param bizCode 业务代码,2位,建议数值
     */
    public static String buildOrderNo(String userId, String bizCode) {
        return build(userId, BANK_UP_2);
    }

    /**
     * 根据指定策略生成订单ID
     */
    private static String build(String userId, String bizCode) {


        StringBuilder sb = new StringBuilder();

        // 18
        sb.append(TSSGenerator.getTSSequence());

        // 2
        sb.append(bizCode);

        // 4
        sb.append(getTableSuffix(userId));

        // 6 服务器IP后6位
        sb.append(getServerIPEnd6());

        // 2
        sb.append(getRandomSuffix());

        return sb.toString();
    }

    private static String getRandomSuffix() {
        int r = random.nextInt(99);
        String rs = String.valueOf(r);
        return StringUtils.leftPad(rs, 2, '0');
    }

    private static String getTableSuffix(String userId) {

        Long userIdLong;

        try {
            userIdLong = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("用户ID无法解析成Long:" + userId);
        }

        Long tableSuffixLong = userIdLong % 10000 % 1024 / 64 * 64;

        return StringUtils.leftPad(String.valueOf(tableSuffixLong), 4, '0');
    }


    private static String getServerIPEnd6() {
        String ips = IPUtils.getLocalIp();
        if (StringUtils.isBlank(ips)) {
            return DEFAULT_IP;
        }
        String[] ipArr = ips.split("\\.");
        if (ipArr.length != 4) {
            return DEFAULT_IP;
        }

        return StringUtils.leftPad(ipArr[2], 3, '0')
                + StringUtils.leftPad(ipArr[3], 3, '0');
    }
}
