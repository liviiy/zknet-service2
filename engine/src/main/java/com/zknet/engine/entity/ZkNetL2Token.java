package com.zknet.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "zknet_l2_token")
public class ZkNetL2Token extends BaseEntity {

    private Long id;

    private String name;

    private String fullName;

    private String contractAddress;

    private String type;

    private Integer decimal;

    private BigInteger tokenId;
}
