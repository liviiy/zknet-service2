package com.zknet.gateway.biz;

import com.zknet.gateway.jsonrpc.dto.ZkNetL2TokenDTO;

import java.math.BigInteger;

public interface L2TokenBiz {
    ZkNetL2TokenDTO getTokenById(Long id);

    ZkNetL2TokenDTO getTokenByTokenId(BigInteger tokenId);
}
