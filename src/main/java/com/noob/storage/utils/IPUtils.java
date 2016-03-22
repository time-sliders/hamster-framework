package com.noob.storage.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * IP工具类
 */
public class IPUtils {

    private static Logger logger = Logger.getLogger(IPUtils.class);

    /**
     * LOCAL_BACK_LOOP IP 本地回环地址
     */
    public static final String LOCAL_BACK_LOOP_IP = "127.0.0.1";

    /**
     * 验证传入的IPv4地址是否在指定的IP区域段内部
     */
    public static boolean checkIPv4IpIn(String startIp, String endIp, String checkIp) {

        if (!checkIsIPv4s(startIp, endIp, checkIp)) {
            return false;
        }

        long start = getIpNum(startIp);
        long end = getIpNum(endIp);
        long check = getIpNum(checkIp);

        return check <= end && check >= start;
    }

    /**
     * 将IP地址中的每一个数字左填充为3位数字如 168.3.4.144 => 168003004144
     */
    public static long getIpNum(String ip) {

        String[] values = StringUtils.split(ip, "[.]");

        StringBuilder ipNumStr = new StringBuilder();

        for (String block : values) {
            String blockNum = StringUtils.leftPad(block, 3, "0");
            ipNumStr.append(blockNum);
        }

        if (ipNumStr.length() > 0 && NumberUtils.isNumber(ipNumStr.toString())) {
            return NumberUtils.toLong(ipNumStr.toString());
        }

        return 0;
    }

    /**
     * 校验传入的IPv4中是否全部都是正确的IP地址
     */
    public static boolean checkIsIPv4s(String... ips) {

        if (ips == null || ips.length <= 0) {
            return false;
        }

        for (String ip : ips) {
            if (!checkIsIPv4(ip)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 校验传入的IPv4是否是正确的IP地址
     */
    public static boolean checkIsIPv4(String ip) {

        return !StringUtils.isBlank(ip) && Pattern.matches(Regex.IPV4_REGEX, ip);

    }

    /**
     * 从request消息头中获取客户端请求IP<br/>
     * <p>需要注意的是:生产环境一般分为办公网和生产网,办公网NAT到生产网的原理是
     * 在办公网部署一台服务器A,生产网授权给A,之后办公网将请求发给A,由A转发给生产网
     * 真正的应用服务器B，这个时候F5的X-forwarded-for消息头中获取到的全是A的地址,
     * 该情况需要考虑其他获取方式</p>
     */
    public static String getClientIp(HttpServletRequest request) {

        String clientIp;

        /**
         * 从F5 x-forwarded-for 消息头中获取<br/>
         * 获取规则：第一个不为unknown的IP
         * */
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(xForwardedFor)) {
            String[] xForwardedForIps = xForwardedFor.split(",");
            for (String xForwardedForIp : xForwardedForIps) {
                if (StringUtils.isNotBlank(xForwardedForIp)
                        && !"unknown".equalsIgnoreCase(xForwardedForIp)) {
                    return xForwardedForIp;
                }
            }
        }

        clientIp = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
            return clientIp;
        }

        clientIp = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
            return clientIp;
        }

        clientIp = request.getHeader("Cdn-Src-Ip");
        if (StringUtils.isNotBlank(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
            return clientIp;
        }

        clientIp = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
            return clientIp;
        }

        clientIp = request.getRemoteAddr();
        //如果客户端就是本地计算机，取不为本地回环地址的IP
        if (LOCAL_BACK_LOOP_IP.equals(clientIp)) {
            clientIp = getLocalIp();
        }


        return clientIp;
    }

    /**
     * 获取本机IP<br/>
     * 本地回环[127.0.0.1] 不会作为返回结果
     */
    public static String getLocalIp() {
        Enumeration<NetworkInterface> nis;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet4Address
                            && !LOCAL_BACK_LOOP_IP.equals(ia.getHostAddress())) {
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
