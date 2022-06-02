package com.zknet.gateway.jsonrpc.param;

import com.zknet.gateway.annotation.PastAndFuture;
import com.zknet.gateway.exception.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositParam {

    /**
     * layer2 account address
     */
    @NotBlank
    private String address;

//    @PastAndFuture(pastSecond = 2 * 60, futureSecond = 2 * 60, bizCodeEnum = BizCodeEnum.LAYER1_TIMESTAMP_EXPIRATION)
    @NotNull
    private Integer timestamp;

    /**
     * tokenId
     */
    @NotNull
    private BigInteger token;

    /**
     * deposit quantity
     */
    @NotBlank
    private String amount;

    @NotBlank(message = "sign not null")
    private String sign;

}
