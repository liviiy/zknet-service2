package com.zknet.engine.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderTypeEnum {
    MARKET_PRICE,
    FIXED_PRICE;

    public static OrderTypeEnum getByName(String name) {
        return Arrays.stream(OrderTypeEnum.values()).filter(e -> e.name().equals(name)).findAny().orElse(MARKET_PRICE);
    }
}
