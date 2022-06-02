package com.zknet.engine.entity;

import com.zknet.engine.constants.SettlementStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "zknet_ex_settlement")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZknetExSettlement extends BaseEntity {
    @Id
    private Long id;

    /**
     * exchange number
     */
    @Column(name = "ex_no")
    private String exNo;

    /**
     * settlement number
     */
    @Column(name = "st_no")
    private String stNo;

    /**
     * order A number
     */
    @Column(name = "order_a_no")
    private String orderANo;

    /**
     * order B number
     */
    @Column(name = "order_b_no")
    private String orderBNo;

    /**
     * order A fee
     */
    @Column(name = "order_a_fee")
    private BigInteger orderAFee;

    /**
     * order B fee
     */
    @Column(name = "order_b_fee")
    private BigInteger orderBFee;


    @Column(name = "tx_hash")
    private String txHash;

    /**
     *
     */
    @Column(name = "`status`")
    private SettlementStatusEnum status;
}
