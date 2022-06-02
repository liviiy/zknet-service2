package com.zknet.gateway.jsonrpc.dto;

import lombok.Data;

@Data
public class DepositDto {
    private String address;

    // tokenName
    private String token;

    // deposit quantity
    private String amount;
}
