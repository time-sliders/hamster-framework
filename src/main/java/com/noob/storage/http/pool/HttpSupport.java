package com.noob.storage.http.pool;

import com.noob.storage.http.HttpClientUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * HTTP接口调用方式的顶级抽象<br/>
 * 主要将post 与 get 请求的调用方式抽象出来
 */
public abstract class HttpSupport implements InitializingBean {

    private static final Logger logger =
            LoggerFactory.getLogger(HttpSupport.class);

    protected static final String HTTPS = "https";

    @Autowired
    protected HttpClientHolder httpClientHolder;

    protected HttpClient getHttpClient() {
        return httpClientHolder.getHttpClient();
    }

    protected abstract int getSoTimeout();

    protected abstract int getConnectTimeout();

    protected abstract String getMimeType();

    protected abstract String getCharset();

    public String get(String url, String param) throws Exception {

        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is unavailable!");
        }

        if (StringUtils.isNotBlank(param)) {
            url += param;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("start > http get-URL:{}", new Object[]{url});
        }

        HttpGet get = new HttpGet(url);

        String result;
        HttpClient client = null;
        try {
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            customReqConf.setConnectTimeout(getConnectTimeout());
            customReqConf.setSocketTimeout(getSoTimeout());

            get.setConfig(customReqConf.build());

            HttpResponse res;

            if (url.startsWith(HTTPS)) {
                // 执行 Https 请求.
                client = HttpClientUtils.createSSLInsecureClient();
                res = client.execute(get);
            } else {
                // 执行 Http 请求.
                client = getHttpClient();
                res = client.execute(get);
            }

            result = IOUtils.toString(res.getEntity().getContent(), getCharset());
        } finally {
            get.releaseConnection();
            if (url.startsWith(HTTPS) && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("end   > http get-result:{}", new Object[]{result});
        }
        return result;
    }

    /**
     * 发送post请求
     */
    public String post(String url, Map<String, String> headers, String body) throws Exception {

        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url is unavailable!");
        }

        String result = null;

        HttpPost post = new HttpPost(url);

        HttpClient client = null;
        try {

            if (StringUtils.isNotBlank(body)) {

                HttpEntity entity = new StringEntity(body, ContentType.create(getMimeType(), getCharset()));
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }

            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            customReqConf.setConnectTimeout(getConnectTimeout());
            customReqConf.setSocketTimeout(getSoTimeout());
            post.setConfig(customReqConf.build());

            HttpResponse res;
            if (url.startsWith(HTTPS)) {
                // 执行 Https 请求.
                client = HttpClientUtils.createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // 执行 Http 请求.
                client = getHttpClient();
                res = client.execute(post);
            }
            result = IOUtils.toString(res.getEntity().getContent(), getCharset());
        } finally {
            post.releaseConnection();
            if (url.startsWith(HTTPS) && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;
    }

    public void afterPropertiesSet() throws Exception {
        if (httpClientHolder == null) {
            throw new IllegalArgumentException("httpClientHolder must not be null!");
        }
    }

}
