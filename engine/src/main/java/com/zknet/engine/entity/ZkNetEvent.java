package com.zknet.engine.entity;

import com.zknet.engine.constants.TransactionFlowTypeEnum;
import com.zknet.engine.constants.TxStatusEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "zknet_event")
public class ZkNetEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * interaction type
     */
    @Column(name = "type")
    private TransactionFlowTypeEnum type;

    @Column(name = "ex_no")
    private String exNo;

    /**
     * operation mode
     */
    private String mode;

    /**
     * transaction status
     */
    @Column(name = "tx_status")
    private TxStatusEnum txStatus;

    /**
     * layer1 transaction hash
     */
    @Column(name = "tx_hash")
    private String txHash;

    /**
     * layer2 transaction hash
     */
    @Column(name = "confirm_hash")
    private String confirmHash;

    /**
     * transaction signature
     */
    @Column(name = "tx_sign")
    private String txSign;

//
//    @Column(name = "error_info")
//    private String errorInfo;

}
