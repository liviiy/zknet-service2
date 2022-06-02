package com.zknet.engine.entity;

import com.zknet.engine.constants.OrderStatusEnum;
import com.zknet.engine.constants.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "zknet_ex_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZknetExOrder extends BaseEntity {
    @Id
    private Long id;

    /**
     * exchange number
     */
    @Column(name = "ex_no")
    private String exNo;

    /**
     * user address
     */
    @Column(name = "user_addr")
    private String userAddr;

    /**
     * order number
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * order type
     */
    @Column(name = "order_type")
    private OrderTypeEnum orderType;

    /**
     *
     */
    @Column(name = "position_id")
    private String positionId;

    @Column(name = "position_token")
    private String positionToken;

    @Column(name = "position_amount")
    private String positionAmount;

    /**
     * order time
     */
    private Integer timestamp;

    private String fee;

    private String extend;

    @Column(name = "`status`")
    private OrderStatusEnum status;

    private String sign;

}
