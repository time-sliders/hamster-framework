package com.noob.storage.http;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1696962467800725315L;

    private Logger logger = Logger.getLogger(BaseServlet.class);

    protected void setRespCharset(HttpServletResponse resp, String charset) {
        if (StringUtils.isNotBlank(charset)) {
            resp.setCharacterEncoding(charset);
        } else {
            logger.warn("EMPTY RESPONSE CHARSET");
        }
    }

}
