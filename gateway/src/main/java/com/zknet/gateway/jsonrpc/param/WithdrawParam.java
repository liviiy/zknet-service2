package com.zknet.gateway.jsonrpc.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawParam {
    // layer2 account address
    @NotBlank
    private String address;

    //current second timestamp
    @NotNull
    private Integer timestamp;

    // tokenId
    @NotNull
    private BigInteger token;

    // withdraw token quantity
    @NotBlank
    private String amount;

    @NotBlank
    private String sign;
}
