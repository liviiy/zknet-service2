package com.zknet.gateway.config.filter;

import com.zknet.gateway.config.BodyReaderHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*",filterName = "CustomFilter")
public class CustomFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("custom filter begin");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            log.debug("------------------------------request message----------------------------------");
            log.debug(getParamsFromRequestBody((HttpServletRequest) requestWrapper));
            log.debug("------------------------------request message----------------------------------");
            chain.doFilter(requestWrapper, response);
        }
    }

    /* *
     * get request body
     * @return
     */
    private String getParamsFromRequestBody(HttpServletRequest request) {
        BufferedReader br = null;
        String listString = "";
        try {
            br = request.getReader();
            String str = "";
            while ((str = br.readLine()) != null) {
                listString += str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listString;
    }

    @Override
    public void destroy() {
        log.debug("custom filter destroy");
    }
}
