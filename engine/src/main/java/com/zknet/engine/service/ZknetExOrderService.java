package com.zknet.engine.service;

import com.zknet.engine.entity.ZknetExOrder;

public interface ZknetExOrderService {
    ZknetExOrder getRecordByExNoAndOrderNo(String exNo, String orderNo);
    int save(ZknetExOrder order);
}
