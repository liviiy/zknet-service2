package com.zknet.message.flowable.param;

import com.zknet.engine.constants.PlatformEnum;
import lombok.*;

import java.math.BigInteger;

@Data
public class DepositEventLogParam extends AbstractEventLogParam {

    public DepositEventLogParam(String contractAddress, String topic, String txHash, Long blockNo, String data, PlatformEnum platformEnum, BigInteger tokenId, String account, BigInteger amount) {
        super(contractAddress, topic, txHash, blockNo, data, platformEnum);
        this.tokenId = tokenId;
        this.account = account;
        this.amount = amount;
    }

    /**
     * token  Token id
     */
    private BigInteger tokenId;

    /**
     * account  Account address
     */
    private String account;

    /**
     * amount  Token amount
     */
    private BigInteger amount;

}
