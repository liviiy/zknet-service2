package com.zknet.gateway.jsonrpc.param;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SettlementParam {
    @NotNull
    private Part partA;
    @NotNull
    private Part partB;
    @NotNull
    private Settlement settlement;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class Part {
        // layer2 account address
        @NotBlank
        private String trader;
        @NotBlank
        private String id;
        @NotBlank
        private String type;
        @NotNull
        private String positionId;
        @NotNull
        private String positionToken;
        @NotBlank
        private String positionAmount;
        @NotNull
        private String timestamp;
        @NotBlank
        private String fee;
        private String extend;
        @NotBlank
        private String signature;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class Settlement {
        /**
         * settlement number
         */
        @NotBlank
        private String stNo;
        @NotBlank
        private String positionSold;
        @NotBlank
        private String partAFee;
        @NotBlank
        private String partBFee;
    }
}
