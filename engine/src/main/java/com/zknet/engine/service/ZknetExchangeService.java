package com.zknet.engine.service;

import com.zknet.engine.entity.ZknetExchange;

public interface ZknetExchangeService {
    ZknetExchange getByExNo(String exNo);

    ZknetExchange getByAddress(String address);

    String getExNoByApiKey(String apiKey);
}
