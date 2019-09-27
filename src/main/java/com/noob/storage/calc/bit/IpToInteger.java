package com.noob.storage.calc.bit;

/**
 * @author 卢云(luyun)
 * @version game over
 * @since 2019.09.24
 */
public class IpToInteger {

    /**
     * @param ip IP地址 "192.168.1.1"
     */
    public static int convertToInteger(String ip) {
        int result = 0;
        String[] array = ip.split("\\.");
        result &= Integer.valueOf(array[0]) << 24;
        result &= Integer.valueOf(array[1]) << 16;
        result &= Integer.valueOf(array[2]) << 8;
        result &= Integer.valueOf(array[3]);
        return result;
    }

}
