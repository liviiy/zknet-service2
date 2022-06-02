package com.zknet.gateway.jsonrpc.dto;

import lombok.Data;

@Data
public class ZkSync2WithdrawDto extends WithdrawDto{

    private String sign;
}
