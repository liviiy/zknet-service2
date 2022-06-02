package com.zknet.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "zknet_contract_log")
@AllArgsConstructor
@NoArgsConstructor
public class ZknetContractLog extends BaseEntity {
    @Id
    private Long id;

    /**
     * tx hash
     */
    @Column(name = "tx_hash")
    private String txHash;

    /**
     * contract address
     */
    @Column(name = "contract_address")
    private String contractAddress;

    /**
     * block number
     */
    @Column(name = "block_no")
    private Long blockNo;

    /**
     *
     */
    private String data;

    /**
     * topic
     */
    private String topic;

    /**
     * from
     */
    @Column(name = "logJson")
    private String logJson;

    /**
     * chain name
     */
    private String platform;
}
