package com.zknet.engine.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public enum TransactionFlowTypeEnum {
    L1TOL2(new HashSet<>(Arrays.asList(TxStatusEnum.L1_FAIL,TxStatusEnum.L1L2_FAIL,TxStatusEnum.L1L2_SUCCESS)),TxStatusEnum.L1L2_SUCCESS,TxStatusEnum.L1L2_FAIL),
    L2TOL1(new HashSet<>(Arrays.asList(TxStatusEnum.L2_FAIL,TxStatusEnum.L2L1_FAIL,TxStatusEnum.L2L1_SUCCESS)),TxStatusEnum.L2L1_SUCCESS,TxStatusEnum.L2L1_FAIL),
    L1(new HashSet<>(Arrays.asList(TxStatusEnum.L1_FAIL,TxStatusEnum.L1_SUCCESS)),TxStatusEnum.L1_SUCCESS,TxStatusEnum.L1_FAIL),
    L2(new HashSet<>(Arrays.asList(TxStatusEnum.L2_FAIL,TxStatusEnum.L2_SUCCESS)),TxStatusEnum.L2_SUCCESS,TxStatusEnum.L2_FAIL);

    private Set<TxStatusEnum> finalStatusSet;
    private TxStatusEnum success;
    private TxStatusEnum fail;

    private TransactionFlowTypeEnum(Set<TxStatusEnum> finalStatusSet,TxStatusEnum success,TxStatusEnum fail) {
        this.finalStatusSet = finalStatusSet;
        this.success = success;
        this.fail = fail;
    }
    public boolean isFinalStatus(TxStatusEnum txStatusEnum) {
        return this.finalStatusSet.contains(txStatusEnum);
    }

    public static TransactionFlowTypeEnum getByName(String name) {
        return Arrays.stream(TransactionFlowTypeEnum.values()).filter(e -> e.name().equals(name)).findAny().orElse(null);
    }
}
