package com.zknet.engine.service.impl;

import com.zknet.engine.cache.CacheConstants;
import com.zknet.engine.entity.ZknetExchange;
import com.zknet.engine.mapper.ZknetExchangeMapper;
import com.zknet.engine.service.ZknetExchangeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ZknetExchangeServiceImpl implements ZknetExchangeService {

    @Resource
    private ZknetExchangeMapper zknetExchangeMapper;
    @Override
    @Cacheable(cacheNames = CacheConstants.EXCHANGE_INFO_CACHE_NAME,keyGenerator = "defaultCacheKeyGenerator",sync = true)
    public ZknetExchange getByExNo(String exNo) {
        ZknetExchange param = new ZknetExchange();
        param.setExNo(exNo);
        ZknetExchange exchange = zknetExchangeMapper.selectOne(param);
        return exchange;
    }

    @Override
    @Cacheable(cacheNames = CacheConstants.EXCHANGE_INFO_CACHE_NAME,keyGenerator = "defaultCacheKeyGenerator",sync = true)
    public ZknetExchange getByAddress(String address) {
        ZknetExchange param = new ZknetExchange();
        param.setAddress(address);
        List<ZknetExchange> list = zknetExchangeMapper.select(param);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Cacheable(cacheNames = CacheConstants.EXCHANGE_INFO_CACHE_NAME,keyGenerator = "defaultCacheKeyGenerator",sync = true)
    public String getExNoByApiKey(String apiKey) {
        ZknetExchange param = new ZknetExchange();
        param.setApiKey(apiKey);
        ZknetExchange exchange = zknetExchangeMapper.selectOne(param);
        return exchange == null ? null : exchange.getExNo();
    }
}
