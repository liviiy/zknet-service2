package com.zknet.engine.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "zknet_exchange")
public class ZknetExchange extends BaseEntity {
    @Id
    private Long id;

    /**
     * exchange no
     */
    @Column(name = "ex_no")
    private String exNo;

    @Column(name = "api_key")
    private String apiKey;

    private String address;

    private String name;
}
