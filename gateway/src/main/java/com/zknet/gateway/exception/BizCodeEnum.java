package com.zknet.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizCodeEnum {
    NOT_AUTH(1106, "NO_AUTH"),
    MISSING_REQUIRED_FIELDS(100001, "MISSING_REQUIRED_FIELDS"),
    LAYER1_SIGN_ERROR(101000, "LAYER1_SIGN_ERROR"),
    LAYER2_SIGN_ERROR(101001, "LAYER2_SIGN_ERROR"),
    LAYER1_TIMESTAMP_EXPIRATION(101002, "LAYER1_TIMESTAMP_EXPIRATION"),
    LAYER2_TIMESTAMP_EXPIRATION(101003, "LAYER2_TIMESTAMP_EXPIRATION"),
    NO_ENOUGH_BALANCE(103000, "NO_ENOUGH_BALANCE"),
    TRANSACTION_FAIL(103001,"TRANSACTION_FAIL"),
    SIGN_REPEAT_SUBMIT(104000, "SIGN_REPEAT_SUBMIT"),
    INTERNAL_ERROR(105000, "INTERNAL_ERROR"),
    TRY_AGAIN_LATER(106000, "TRY_AGAIN_LATER"),

    //SETTLEMENT RELATED
    ORDER_FINISHED(107000,"ORDER_ALREADY_FINISHED"),
    SETTLEMENT_REPEAT_SUBMIT(107001, "SETTLEMENT_REPEAT_SUBMIT"),

    //EXCHANGE RELATED
    EXCHANGE_HAS_NO_CONTRACT_RECORD(108000,"EXCHANGE_HAS_NO_CONTRACT_RECORD"),
    EXCHANGE_HAS_NO_INFO_RECORD(108001,"EXCHANGE_HAS_NO_INFO_RECORD"),

    RATE_LIMIT(429,"RATE_LIMITED"),
    ;
    /**
     * 业务码
     */
    private final int code;

    /**
     * 描述信息
     */
    private final String msg;
}
