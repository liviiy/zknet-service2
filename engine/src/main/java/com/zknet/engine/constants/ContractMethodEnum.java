package com.zknet.engine.constants;


import lombok.Getter;

@Getter
public enum ContractMethodEnum {

    DEPOSIT("deposit","LogDeposit"),

    WITHDRAW("withdraw","LogWithdrawn"),

    BIND_ACCOUNT("bind",""),

    SETTLEMENT("settlement",""),
    ;

    ContractMethodEnum(String contractMethod, String event) {
        this.method = contractMethod;
        this.event = event;
    }

    /**
     * invoke contract method name
     */
    private String method;

    /**
     * event name
     */
    private String event;


}
