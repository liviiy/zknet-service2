package com.zknet.engine.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "zknet_contract_log_event_handle_progress")
public class ZknetContractLogEventHandleProgress extends BaseEntity {
    @Id
    private Long id;

    /**
     * topic
     */
    @Column(name = "topic")
    private String topic;

    /**
     * chain name
     */
    private String platform;

    /**
     * current completed block no
     */
    @Column(name = "complete_block_no")
    private Long completeBlockNo;

}
