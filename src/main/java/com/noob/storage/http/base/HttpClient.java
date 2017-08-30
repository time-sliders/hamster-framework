package com.noob.storage.http.base;

import com.noob.storage.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/***
 * HTTP 服务客户端程序
 * <p/>
 * <ol>
 * <li>The connection object is created by invoking the
 * {@code openConnection} method on a URL.
 * <li>The setup parameters and general request properties are manipulated.
 * <li>The actual connection to the remote object is made, using the
 * {@code connect} method.
 * <li>The remote object becomes available. The header fields and the contents
 * of the remote object can be accessed.
 * </ol>
 * <p/>
 */
public class HttpClient {

    private Logger logger = Logger.getLogger(HttpClient.class);

    private static final int DEFAULT_TIMEOUT = 60000;

    protected HttpURLConnection conn = null;

    /**
     * 初始化连接对象,调用完该方法之后，与服务器的HTTP连接并没有建立
     * 可以在之后添加相关属性
     */
    public HttpClient(String URLAddress) throws IOException {
        conn = (HttpURLConnection) new URL(URLAddress).openConnection();
        conn.setReadTimeout(DEFAULT_TIMEOUT);
        conn.setConnectTimeout(DEFAULT_TIMEOUT);
        conn.setUseCaches(false);
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Charset", "UTF-8");
    }

    /**
     * 创建与HTTP服务器的link.<br/>
     * 该方法调用完毕就不可以再对HttpURLConnection对象连接属性做设置了
     *
     * @throws BusinessException 连接不上服务器
     */
    public void connect() throws BusinessException {
        try {
            conn.connect();
        } catch (IOException e) {
            logger.error("无法与HTTP服务器[" + conn.getURL().toString() + "]建立连接", e);
            throw new BusinessException("无法与HTTP服务器[" + conn.getURL().toString() + "]建立连接");
        }
    }

    /**
     * 从服务端读取返回信息<br/>
     * <b>
     * HTTP连接中服务端只可以返回一次消息,所以该方法在一个instance中只可以调用一次
     * </b>
     */
    public String read() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int count = 0;
            byte[] buf = new byte[1024 * 8];
            while ((count = conn.getInputStream().read(buf)) != -1) {
                bos.write(buf, 0, count);
                bos.flush();
            }
            return new String(bos.toByteArray(), "UTF-8");
        }
    }

    /**
     * 根据列表参数，组织HTTP参数信息
     */
    protected String generateParams(List<Property> properties) {

        if(CollectionUtils.isEmpty(properties)){
            return  null;
        }

        StringBuilder sb = new StringBuilder();
        for (Property property : properties) {
            sb.append(property != null ? property : "");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return sb.toString();
    }

}
