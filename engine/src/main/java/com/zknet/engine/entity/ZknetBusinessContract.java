package com.zknet.engine.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "zknet_business_contract")
public class ZknetBusinessContract extends BaseEntity {
    @Id
    private Long id;

    /**
     * contract_address
     */
    @Column(name = "contract_address")
    private String contractAddress;

    /**
     * chain name
     */
    private String platform;

    /**
     * chain id
     */
    @Column(name = "chain_id")
    private Long chainId;

    /**
     * contract owner
     */
    private String owner;

    /**
     * exchange number
     */
    @Column(name = "ex_no")
    private String exNo;
}
