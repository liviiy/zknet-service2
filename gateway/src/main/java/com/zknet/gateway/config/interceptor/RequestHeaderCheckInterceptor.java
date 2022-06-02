package com.zknet.gateway.config.interceptor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zknet.gateway.biz.TxBiz;
import com.zknet.gateway.config.RequestHeaderContext;
import com.zknet.gateway.exception.BizCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class RequestHeaderCheckInterceptor implements HandlerInterceptor {
    private static final String HEAD_KEY = "x-api-key";
    private static final Set<String> NO_NEED_CHECK_HEADER_METHOD_SET = new HashSet<>(Arrays.asList("zkn_getApiKey"));

    @Resource
    private TxBiz zkSync2TxBiz;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader(HEAD_KEY);
        String params = getParamsFromRequestBody(request);
        log.debug("RequestHeaderCheckInterceptor get params:{}",params);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONUtil.parseObj(params);
        } catch (Exception e) {
            writeResponse(buildNonJsonContent(null),response);
            return false;
        }
        String method = jsonObject.getStr("method");
        if (NO_NEED_CHECK_HEADER_METHOD_SET.contains(method)) {
            return true;
        }
        String id = jsonObject.getStr("id");
        if (checkApiKey(apiKey)) {
            return true;
        }
        writeResponse(buildNoAuthContent(id),response);
        return false;
    }
    private void writeResponse(String content,HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(content);
    }
    private String buildNoAuthContent(String id) {
        JSONObject resp = new JSONObject();
        resp.set("jsonrpc","2.0");
        resp.set("id",id);
        JSONObject err = new JSONObject();
        err.set("code", BizCodeEnum.NOT_AUTH.getCode());
        err.set("message",BizCodeEnum.NOT_AUTH.getMsg());
        resp.set("error",err);
        return resp.toString();
    }
    private String buildNonJsonContent(String id) {
        JSONObject resp = new JSONObject();
        resp.set("jsonrpc","2.0");
        resp.set("id",id);
        JSONObject err = new JSONObject();
        err.set("code", -32700);
        err.set("message","JSON parse error");
        resp.set("error",err);
        return resp.toString();
    }
    private boolean checkApiKey(String apiKey) {
        if (StringUtils.hasText(apiKey)) {
            String exNo = zkSync2TxBiz.getExNoByApiKey(apiKey);
            if (StringUtils.hasText(exNo)) {
                new RequestHeaderContext.RequestHeaderContextBuild().exNo(exNo,apiKey).bulid();
                return true;
            }
            log.warn("api_key:{} has no exchange info",apiKey);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestHeaderContext.clean();
    }

    private String getParamsFromRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        try {
            String line = null;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


