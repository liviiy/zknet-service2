package com.zknet.gateway.config;

import org.springframework.util.StringUtils;

public class RequestHeaderContext {
    private static final ThreadLocal<RequestHeaderContext> REQUEST_HEADER_CONTEXT_THREAD_LOCAL=new ThreadLocal<>();
    // exchanger id
    private String exNo;

    private String apiKey;

    public String getExNo() {
        return exNo;
    }
    public String getApiKey() {
        return apiKey;
    }

    public static RequestHeaderContext getInstance(){
        return REQUEST_HEADER_CONTEXT_THREAD_LOCAL.get();
    }

    public void setContext(RequestHeaderContext context){
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static void clean(){
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.remove();
    }

    private RequestHeaderContext(RequestHeaderContextBuild requestHeaderContextBuild){
        this.exNo=requestHeaderContextBuild.exNo;
        this.apiKey = requestHeaderContextBuild.apiKey;
        setContext(this);
    }

    public static class RequestHeaderContextBuild{
        private String exNo;
        private String apiKey;

        public RequestHeaderContextBuild exNo(String exNo,String apiKey){
            this.exNo=exNo;
            this.apiKey = apiKey;
            return this;
        }
        public RequestHeaderContext bulid(){
            return new RequestHeaderContext(this);
        }
    }
}
