package com.zknet.engine.service;

import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.entity.ZknetBusinessContract;

import java.util.List;

public interface ZknetBusinessContractService {
    List<ZknetBusinessContract> getListByPlatform(PlatformEnum platformEnum);
    ZknetBusinessContract getByExNoAndPlatform(String exNo, PlatformEnum platformEnum);

}
