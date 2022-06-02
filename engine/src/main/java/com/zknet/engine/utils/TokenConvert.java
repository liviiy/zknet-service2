package com.zknet.engine.utils;

import java.math.BigDecimal;

public class TokenConvert {
    public static long toUnitAmount(String amount, int decimal) {
        BigDecimal unitAmount = new BigDecimal(amount).multiply(BigDecimal.TEN.pow(decimal));
        if (unitAmount.compareTo(BigDecimal.ONE) < 0) {
            throw new RuntimeException("invalid token amount");
        }
        return unitAmount.longValue();
    }
}
