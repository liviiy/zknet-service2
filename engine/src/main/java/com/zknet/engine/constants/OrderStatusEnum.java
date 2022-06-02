package com.zknet.engine.constants;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    FINISHED,
    UNFINISHED;

    public static boolean isFinished(OrderStatusEnum status) {
        return FINISHED == status;
    }
}
