package com.zknet.engine.service.impl;

import com.zknet.engine.cache.CacheConstants;
import com.zknet.engine.entity.ZkNetL2Token;
import com.zknet.engine.mapper.ZkNetL2TokenMapper;
import com.zknet.engine.service.ZkNetL2TokenService;
import com.zknet.engine.utils.TokenConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
@Slf4j
public class ZkNetL2TokenServiceImpl implements ZkNetL2TokenService {

    @Resource
    private ZkNetL2TokenMapper zkNetL2TokenMapper;

    @Override
    @Cacheable(cacheNames = CacheConstants.L2_TOKEN_CACHE_NAME,keyGenerator = "defaultCacheKeyGenerator",sync = true)
    public ZkNetL2Token getById(Long id) {
        return zkNetL2TokenMapper.getById(id);
    }

    @Override
    public long getUnitAmount(String name, String amount) {
        return TokenConvert.toUnitAmount(amount, getTokenByName(name).getDecimal());
    }

    @Override
    public long getUnitAmount(BigInteger tokenId, String amount) {
        return TokenConvert.toUnitAmount(amount, getTokenByTokenId(tokenId).getDecimal());
    }

    @Override
    public String getTokenAddress(String name) {
        return getTokenByName(name).getContractAddress();
    }

    @Override
    @Cacheable(cacheNames = CacheConstants.L2_TOKEN_CACHE_NAME,keyGenerator = "defaultCacheKeyGenerator",sync = true)
    public ZkNetL2Token getTokenByName(String name) {
        ZkNetL2Token param = new ZkNetL2Token();
        param.setName(name);
        ZkNetL2Token token = zkNetL2TokenMapper.selectOne(param);
        if (token == null) {
            throw new RuntimeException("invalid token name");
        }
        return token;
    }

    @Override
    @Cacheable(cacheNames = CacheConstants.L2_TOKEN_CACHE_NAME,keyGenerator = "defaultCacheKeyGenerator",sync = true)
    public ZkNetL2Token getTokenByTokenId(BigInteger tokenId) {
        ZkNetL2Token param = new ZkNetL2Token();
        param.setTokenId(tokenId);
        ZkNetL2Token token = zkNetL2TokenMapper.selectOne(param);
        if (token == null) {
            throw new RuntimeException("invalid token id");
        }
        return token;
    }

}
