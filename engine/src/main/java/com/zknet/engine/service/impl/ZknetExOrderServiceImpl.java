package com.zknet.engine.service.impl;

import com.zknet.engine.entity.ZknetExOrder;
import com.zknet.engine.mapper.ZknetExOrderMapper;
import com.zknet.engine.service.ZknetExOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZknetExOrderServiceImpl implements ZknetExOrderService {

    @Resource
    private ZknetExOrderMapper zknetExOrderMapper;

    @Override
    public ZknetExOrder getRecordByExNoAndOrderNo(String exNo, String orderNo) {
        ZknetExOrder param = new ZknetExOrder();
        param.setExNo(exNo);
        param.setOrderNo(orderNo);
        return zknetExOrderMapper.selectOne(param);
    }

    @Override
    public int save(ZknetExOrder order) {
        if (getRecordByExNoAndOrderNo(order.getExNo(),order.getOrderNo()) != null) {
            return 0;
        }
        return zknetExOrderMapper.insert(order);
    }
}
