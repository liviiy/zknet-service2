package com.zknet.engine.service;

import com.zknet.engine.entity.ZknetExOrder;
import com.zknet.engine.entity.ZknetExSettlement;

public interface ZknetExSettlementService {

    ZknetExSettlement getRecordByExNoAndStNo(String exNo, String settlementNo);
    int save(ZknetExSettlement settlement);

    int saveSettlementAndOrder(ZknetExSettlement settlement, ZknetExOrder orderA, ZknetExOrder orderB);
}
