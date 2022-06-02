package com.zknet.engine.service.impl;

import com.zknet.engine.entity.ZknetExOrder;
import com.zknet.engine.entity.ZknetExSettlement;
import com.zknet.engine.mapper.ZknetExOrderMapper;
import com.zknet.engine.mapper.ZknetExSettlementMapper;
import com.zknet.engine.service.ZknetExSettlementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ZknetExSettlementServiceImpl implements ZknetExSettlementService {

    @Resource
    private ZknetExSettlementMapper zknetExSettlementMapper;

    @Resource
    private ZknetExOrderMapper zknetExOrderMapper;

    @Override
    public ZknetExSettlement getRecordByExNoAndStNo(String exNo, String settlementNo) {
        ZknetExSettlement param = new ZknetExSettlement();
        param.setExNo(exNo);
        param.setStNo(settlementNo);
        return zknetExSettlementMapper.selectOne(param);
    }

    @Override
    public int save(ZknetExSettlement settlement) {
        return zknetExSettlementMapper.insertSelective(settlement);
    }

    @Override
    @Transactional
    public int saveSettlementAndOrder(ZknetExSettlement settlement, ZknetExOrder orderA, ZknetExOrder orderB) {
        if (getOrderRecordByExNoAndOrderNo(orderA.getExNo(),orderA.getOrderNo()) == null) {
            zknetExOrderMapper.insertSelective(orderA);
        }
        if (getOrderRecordByExNoAndOrderNo(orderB.getExNo(),orderB.getOrderNo()) == null) {
            zknetExOrderMapper.insertSelective(orderB);
        }
        return zknetExSettlementMapper.insertSelective(settlement);
    }

    private ZknetExOrder getOrderRecordByExNoAndOrderNo(String exNo, String orderNo) {
        ZknetExOrder param = new ZknetExOrder();
        param.setExNo(exNo);
        param.setOrderNo(orderNo);
        return zknetExOrderMapper.selectOne(param);
    }
}
