package com.zknet.gateway.jsonrpc.param;

import lombok.Data;

@Data
public class BindAccountParam {

    private String address1;

    private Integer timestamp1;

    private String sign1;

    private String address2;

    private Integer timestamp2;

    private String sign2;
}
