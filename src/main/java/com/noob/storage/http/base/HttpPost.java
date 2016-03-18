package com.noob.storage.http.base;


import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HttpPost extends HttpClient {

    public HttpPost(String URLAddress) throws IOException {
        super(URLAddress);
        conn.setRequestMethod(Http.GET.toString());
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
    }

    /**
     * POST方式提交影像时,参数信息需要通过输出流发送到服务器
     */
    public void writeParamsToServer(List<Property> properties) {
        try {
            String s;
            if (StringUtils.isNotBlank((s = generateParams(properties)))) {
                conn.getOutputStream().write(s.getBytes("UTF-8"));
                conn.getOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将输入流中的内容转发到服务器，不会关闭输入流
     */
    public void writeStreamToServer(InputStream is) throws IOException {
        byte[] buf = new byte[1024 * 8];
        int count = 0;
        while ((count = is.read(buf)) != -1) {
            conn.getOutputStream().write(buf, 0, count);
            conn.getOutputStream().flush();
        }
    }

}
