package com.zknet.engine.service.impl;

import com.zknet.engine.entity.ZkNetEvent;
import com.zknet.engine.mapper.ZkNetEventMapper;
import com.zknet.engine.service.ZkNetEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZkNetEventServiceImpl implements ZkNetEventService {

    @Resource
    private ZkNetEventMapper zkNetEventMapper;


    @Override
    public Integer save(ZkNetEvent zkNetDeposit) {
        return zkNetEventMapper.insertSelective(zkNetDeposit);
    }

    @Override
    public Integer update(ZkNetEvent zkNetDeposit) {
        return zkNetEventMapper.updateByPrimaryKeySelective(zkNetDeposit);
    }

    @Override
    public ZkNetEvent findByTxSign(String l1Sign) {
        ZkNetEvent zkNetEvent=new ZkNetEvent();
        zkNetEvent.setTxSign(l1Sign);
        return zkNetEventMapper.selectOne(zkNetEvent);
    }

    @Override
    public ZkNetEvent findByTxHash(String txHash) {
        ZkNetEvent zkNetEvent=new ZkNetEvent();
        zkNetEvent.setTxHash(txHash);
        return zkNetEventMapper.selectOne(zkNetEvent);
    }
}
