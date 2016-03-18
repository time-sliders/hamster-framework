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
     * LOCAL_BACKLOOP IP 本地回环地址
     */
    private static final String LOCAL_BACKLOOP_IP = "127.0.0.1";

    /**
     * 验证传入的IPv4地址是否在指定的IP区域段内部
     */
    public static boolean checkIPv4IpIn(String startIp, String endIp, String checkIp) {
        boolean result = false;
        if (checkIsIPv4s(startIp, endIp, checkIp)) {
            long start = getIpNum(startIp);
            long end = getIpNum(endIp);
            long check = getIpNum(checkIp);
            if (check <= end && check >= start) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 将IP地址中的每一个数字左填充为3位数字如 168.3.4.144 => 168003004144
     */
    public static long getIpNum(String ip) {
        String[] values = StringUtils.split(ip, "[.]");
        StringBuffer ipNumStr = new StringBuffer();
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
     * 校验传入的IPv4是否是正确的IP地址
     */
    public static boolean checkIsIPv4(String ip) {
        boolean result = false;
        if (StringUtils.isNotBlank(ip)) {
            result = Pattern.matches(Regex.IPV4_REGEX, ip);
        }
        return result;
    }

    /**
     * 校验传入的IPv4中是否全部都是正确的IP地址
     */
    public static boolean checkIsIPv4s(String... ips) {
        boolean result = true;
        if (ips != null && ips.length > 0) {
            for (String ip : ips) {
                if (!checkIsIPv4(ip)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 从request消息头中获取客户端请求IP<br/>
     * 需要注意的是:生产环境一般分为办公网和生产网,办公网NAT到生产网的原理是<br/>
     * 在办公网部署一台服务器A,生产网授权给A,之后办公网将请求发给A,由A转发给生产网<br/>
     * 真正的应用服务器B，这个时候F5的X-forwarded-for消息头中获取到的全是A的地址,<br/>
     * 该情况需要考虑其他获取方式
     */
    public static String getClientIp(HttpServletRequest request) {
        String clientIp = null;
        /**判断页面有没有传入客户端IP字段*/
        clientIp = request.getParameter("clientIp");
        if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            /**
             * 从F5 x-forwarded-for 消息头中获取<br/>
             * 获取规则：第一个不为unknown的IP
             * */
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(xForwardedFor)) {
                String[] xForwardedForIps = xForwardedFor.split(",");
                for (int i = 0; i < xForwardedForIps.length; i++) {
                    if (StringUtils.isNotBlank(xForwardedForIps[i])
                            && !"unknown".equalsIgnoreCase(xForwardedForIps[i])) {
                        clientIp = xForwardedForIps[i];
                        break;
                    }
                }
            } else if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getHeader("Proxy-Client-IP");
                if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                    clientIp = request.getHeader("WL-Proxy-Client-IP");
                    if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                        clientIp = request.getHeader("Cdn-Src-Ip");
                        if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                            clientIp = request.getHeader("X-Real-IP");
                            if (StringUtils.isBlank(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
                                clientIp = request.getRemoteAddr();
                                //如果客户端就是本地计算机，取不为本地回环地址的IP
                                if (LOCAL_BACKLOOP_IP.equals(clientIp)) {
                                    clientIp = getLocalIp();
                                }
                            }
                        }
                    }
                }
            }
        }
        return clientIp != null ? clientIp.trim() : clientIp;
    }

    /**
     * 获取本机IP<br/>
     * 本地回环[127.0.0.1] 不会作为返回结果
     */
    public static String getLocalIp() {
        String localIp = null;
        Enumeration<NetworkInterface> nis = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            for (; nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                for (; ias.hasMoreElements(); ) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet4Address
                            && !LOCAL_BACKLOOP_IP.equals(ia.getHostAddress())) {
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e.getMessage(), e);
        }
        return localIp;
    }

}
