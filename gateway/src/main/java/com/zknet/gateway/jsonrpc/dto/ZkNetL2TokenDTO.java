package com.zknet.gateway.jsonrpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZkNetL2TokenDTO {

    private Long id;

    private BigInteger tokenId;

    private String name;

    private String fullName;

    private String contractAddress;

    private String type;
}
