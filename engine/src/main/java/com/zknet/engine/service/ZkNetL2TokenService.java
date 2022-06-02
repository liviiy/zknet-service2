package com.zknet.engine.service;

import com.zknet.engine.entity.ZkNetL2Token;

import java.math.BigInteger;


public interface ZkNetL2TokenService {
    ZkNetL2Token getById(Long id);
    long getUnitAmount(String name,String amount);

    long getUnitAmount(BigInteger tokenId,String amount);

    String getTokenAddress(String name);

    ZkNetL2Token getTokenByName(String name);
    ZkNetL2Token getTokenByTokenId(BigInteger tokenId);
}
