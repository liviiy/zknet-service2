package com.zknet.engine.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseEntity {

    private Timestamp createTime;

    private Timestamp updateTime;
}
