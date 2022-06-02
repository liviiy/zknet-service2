package com.zknet.gateway.jsonrpc.param;

import lombok.Data;

@Data
public class ApiKeyQueryParam {
    private String from;
    private Integer timestamp;
    private String sign;
}
