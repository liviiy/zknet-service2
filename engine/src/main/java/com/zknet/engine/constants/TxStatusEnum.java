package com.zknet.engine.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TxStatusEnum {
    SUCCESS,
    FAIL,
    PENDING,
    NO_DATA,

    // l1 status
    L1_PENDING,
    L1_SUCCESS,
    L1_FAIL,

    // l2 status
    L2_PENDING,
    L2_SUCCESS,
    L2_FAIL,

    // L2->L1 final state ,such as withdraw
    L2L1_SUCCESS,
    L2L1_FAIL,

    // L1->L2 final state,such as deposit
    L1L2_SUCCESS,
    L1L2_FAIL,
    ;

    public static TxStatusEnum getByName(String name) {
        return Arrays.stream(TxStatusEnum.values()).filter(e -> e.name().equals(name)).findAny().orElse(null);
    }
}
