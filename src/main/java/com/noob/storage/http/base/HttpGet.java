package com.noob.storage.http.base;

import com.noob.storage.exception.BusinessException;

import java.io.IOException;

public class HttpGet extends HttpClient {

    public HttpGet(String URLAddress) throws IOException, BusinessException {
        super(URLAddress);
        conn.setRequestMethod(Http.GET.toString());
        conn.setRequestProperty("Content-Type", "text/html");
    }

}
