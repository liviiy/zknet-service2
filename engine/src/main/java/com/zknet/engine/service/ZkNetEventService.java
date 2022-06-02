package com.zknet.engine.service;


import com.zknet.engine.entity.ZkNetEvent;

public interface ZkNetEventService {

    Integer save(ZkNetEvent zkNetDeposit);

    Integer update(ZkNetEvent zkNetDeposit);

    ZkNetEvent findByTxSign(String l1Sign);

    ZkNetEvent findByTxHash(String txHash);
}
