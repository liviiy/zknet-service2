package com.zknet.gateway.jsonrpc.dto;

import lombok.Data;

@Data
public class WithdrawDto {
    // layer2 account address
    private String address2;

    // tokenName
    private String token;

    // deposit quantity
    private String amount;

}
