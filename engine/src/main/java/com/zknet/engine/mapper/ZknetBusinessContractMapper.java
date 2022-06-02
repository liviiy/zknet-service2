package com.zknet.engine.mapper;

import com.zknet.engine.entity.ZknetBusinessContract;

import java.util.List;

public interface ZknetBusinessContractMapper extends BaseMapper<ZknetBusinessContract> {
    List<ZknetBusinessContract> getListByPlatform(String platform);
}
