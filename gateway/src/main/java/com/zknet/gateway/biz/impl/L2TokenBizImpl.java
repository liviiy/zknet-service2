package com.zknet.gateway.biz.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zknet.gateway.biz.L2TokenBiz;
import com.zknet.engine.entity.ZkNetL2Token;
import com.zknet.gateway.jsonrpc.dto.ZkNetL2TokenDTO;
import com.zknet.engine.service.ZkNetL2TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * Convention：Biz can not contains Mapper,only inject Service
 */
@Service
@Slf4j
public class L2TokenBizImpl implements L2TokenBiz {

    @Resource
    private ZkNetL2TokenService zkNetL2TokenService;

    @Override
    public ZkNetL2TokenDTO getTokenById(Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        ZkNetL2Token zkNetL2Token = zkNetL2TokenService.getById(id);
        if (zkNetL2Token != null) {
            ZkNetL2TokenDTO dto = new ZkNetL2TokenDTO();
            BeanUtils.copyProperties(zkNetL2Token, dto);
            return dto;
        }
        return null;
    }

    @Override
    public ZkNetL2TokenDTO getTokenByTokenId(BigInteger tokenId) {
        ZkNetL2Token zkNetL2Token = zkNetL2TokenService.getTokenByTokenId(tokenId);
        ZkNetL2TokenDTO dto = new ZkNetL2TokenDTO();
        BeanUtils.copyProperties(zkNetL2Token, dto);
        return dto;
    }
}
