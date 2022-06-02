package com.zknet.engine.service.impl;

import com.zknet.engine.entity.ZknetContractLog;
import com.zknet.engine.mapper.ZknetContractLogMapper;
import com.zknet.engine.service.ZknetContractLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZknetContractLogServiceImpl implements ZknetContractLogService {

    @Resource
    private ZknetContractLogMapper zknetContractLogMapper;

    @Override
    public int save(ZknetContractLog zknetContractLog) {
        return zknetContractLogMapper.insertSelective(zknetContractLog);
    }
}
