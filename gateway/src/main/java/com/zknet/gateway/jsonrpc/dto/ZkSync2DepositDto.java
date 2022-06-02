package com.zknet.gateway.jsonrpc.dto;

import lombok.Data;

@Data
public class ZkSync2DepositDto extends DepositDto{
    private String sign;
}
