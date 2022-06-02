package com.zknet.engine.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PlatformEnum {
    ZKSYNC2,
    ETH;

    public static PlatformEnum getByName(String name) {
        return Arrays.stream(PlatformEnum.values()).filter(e -> e.name().equals(name)).findAny().orElse(null);
    }
}
