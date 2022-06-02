package com.zknet.engine.service.impl;

import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.entity.ZknetBusinessContract;
import com.zknet.engine.mapper.ZknetBusinessContractMapper;
import com.zknet.engine.service.ZknetBusinessContractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZknetBusinessContractServiceImpl implements ZknetBusinessContractService {

    @Resource
    private ZknetBusinessContractMapper zknetBusinessContractMapper;
    @Override
    public List<ZknetBusinessContract> getListByPlatform(PlatformEnum platformEnum) {
        return zknetBusinessContractMapper.getListByPlatform(platformEnum.name());
    }

    @Override
    public ZknetBusinessContract getByExNoAndPlatform(String exNo, PlatformEnum platformEnum) {
        ZknetBusinessContract param = new ZknetBusinessContract();
        param.setExNo(exNo);
        param.setPlatform(platformEnum.name());
        List<ZknetBusinessContract> list = zknetBusinessContractMapper.select(param);
        return list.isEmpty() ? null : list.get(0);
    }
}
