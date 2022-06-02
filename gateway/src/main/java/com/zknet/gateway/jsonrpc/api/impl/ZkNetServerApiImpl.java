package com.zknet.gateway.jsonrpc.api.impl;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import com.zknet.engine.constants.ContractMethodEnum;
import com.zknet.engine.constants.TxStatusEnum;
import com.zknet.gateway.biz.L2TokenBiz;
import com.zknet.gateway.biz.TxBiz;
import com.zknet.gateway.config.RequestHeaderContext;
import com.zknet.gateway.exception.BizCodeEnum;
import com.zknet.gateway.exception.BizException;
import com.zknet.gateway.jsonrpc.api.ZkNetServerApi;
import com.zknet.gateway.jsonrpc.dto.ZkSync2DepositDto;
import com.zknet.gateway.jsonrpc.dto.ZkNetL2TokenDTO;
import com.zknet.gateway.jsonrpc.dto.ZkSync2WithdrawDto;
import com.zknet.gateway.jsonrpc.param.*;
import com.zknet.gateway.limit.LimitType;
import com.zknet.gateway.limit.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

@Validated
@Service
@AutoJsonRpcServiceImpl
@Slf4j
public class ZkNetServerApiImpl implements ZkNetServerApi {

    @Resource
    private L2TokenBiz l2TokenBiz;

    @Resource
    private TxBiz ethTxBiz;

    @Resource
    private TxBiz zkSync2TxBiz;



    @Override
    @RateLimiter(time = 60,count = 60,limitType = LimitType.API_KEY)
    public ZkNetL2TokenDTO zkn_getTokenById(Long id) {
        return l2TokenBiz.getTokenById(id);
    }

    @SneakyThrows
    @Override
    @RateLimiter(time = 60,count = 60,limitType = LimitType.API_KEY)
    public TxStatusEnum zkn_txStatusByHash(String hash) {
        log.info("exNo:{}", RequestHeaderContext.getInstance().getExNo());
        return zkSync2TxBiz.getTxStatus(hash);
    }

    @Override
    @RateLimiter(time = 60,count = 60,limitType = LimitType.IP)
    public String zkn_getApiKey(ApiKeyQueryParam param) {
        //todo verify sign

        return zkSync2TxBiz.getApiKey(param);
    }

    @Override
    public String bindAccount(BindAccountParam param) {

        SignedRawTransaction signedRawTransaction = this.signVerify(param.getAddress2(), param.getSign1(), BizCodeEnum.LAYER1_SIGN_ERROR);

        Function function = new Function(ContractMethodEnum.BIND_ACCOUNT.getMethod(),
                Arrays.<Type>asList(
                        new Utf8String(param.getAddress1()),
                        new Utf8String(param.getTimestamp1().toString()),
                        new Utf8String(param.getSign1())),
                Collections.<TypeReference<?>>emptyList());

        //  data verification
        if (!String.format("0x%s", signedRawTransaction.getData()).equals(FunctionEncoder.encode(function))) {
            throw new BizException(BizCodeEnum.LAYER1_SIGN_ERROR);
        }

        SignedRawTransaction signedRawTransaction2 = this.signVerify(param.getAddress1(), param.getSign2(), BizCodeEnum.LAYER2_SIGN_ERROR);

        Function function2 = new Function(ContractMethodEnum.BIND_ACCOUNT.getMethod(),
                Arrays.<Type>asList(
                        new Utf8String(param.getAddress2()),
                        new Utf8String(param.getTimestamp2().toString()),
                        new Utf8String(param.getSign2())),
                Collections.<TypeReference<?>>emptyList());

        //  data verification
        if (!String.format("0x%s", signedRawTransaction.getData()).equals(FunctionEncoder.encode(function))) {
            throw new BizException(BizCodeEnum.LAYER2_SIGN_ERROR);
        }

        return zkSync2TxBiz.bindAccount(param);
    }

    @SneakyThrows
    @Override
    @RateLimiter(time = 60,count = 60,limitType = LimitType.API_KEY)
    public String deposit(DepositParam param) {
        ZkSync2DepositDto depositDto = new ZkSync2DepositDto();
        BeanUtils.copyProperties(param, depositDto);

        SignedRawTransaction signedRawTransaction = this.signVerify(param.getAddress(), param.getSign(), BizCodeEnum.LAYER2_SIGN_ERROR);

        // database check token
        l2TokenBiz.getTokenByTokenId(param.getToken());

        //  ERC20 deposit parameter structure
        Function function = new Function(ContractMethodEnum.DEPOSIT.getMethod(),
                Arrays.<Type>asList(
                        new Uint32(param.getToken()),
                        new Uint256(BigInteger.valueOf(Long.valueOf(param.getAmount())))),
                Collections.<TypeReference<?>>emptyList());

        //  data verification
        if (!String.format("0x%s", signedRawTransaction.getData()).equals(FunctionEncoder.encode(function))) {
            throw new BizException(BizCodeEnum.LAYER2_SIGN_ERROR);
        }

        // data recording and broadcasting transactions
        return zkSync2TxBiz.deposit(depositDto);
    }

    @SneakyThrows
    @Override
    @RateLimiter(time = 60,count = 60,limitType = LimitType.API_KEY)
    public String withdraw(WithdrawParam param) {
        ZkSync2WithdrawDto withdrawDto = new ZkSync2WithdrawDto();
        BeanUtils.copyProperties(param, withdrawDto);

        SignedRawTransaction signedRawTransaction = this.signVerify(param.getAddress(), param.getSign(), BizCodeEnum.LAYER2_SIGN_ERROR);

        // database check token
        l2TokenBiz.getTokenByTokenId(param.getToken());

        //  withdraw parameter structure
        Function function = new Function(ContractMethodEnum.WITHDRAW.getMethod(),
                Arrays.<Type>asList(
                        new Uint32(param.getToken()),
                        new Uint256(BigInteger.valueOf(Long.valueOf(param.getAmount())))),
                Collections.<TypeReference<?>>emptyList());

        //  data verification
        if (!String.format("0x%s", signedRawTransaction.getData()).equals(FunctionEncoder.encode(function))) {
            throw new BizException(BizCodeEnum.LAYER2_SIGN_ERROR);
        }

        //add delayed task
        return zkSync2TxBiz.withdraw(withdrawDto);
    }

    @Override
    @RateLimiter(time = 60,count = 60,limitType = LimitType.API_KEY)
    public String settlement(SettlementParam param) {
//        SignedRawTransaction partASignedRawTransaction = this.signVerify(param.getPartA().getTrader(), param.getPartA().getSignature(), BizCodeEnum.LAYER2_SIGN_ERROR);
//        SignedRawTransaction partBSignedRawTransaction = this.signVerify(param.getPartB().getTrader(), param.getPartB().getSignature(), BizCodeEnum.LAYER2_SIGN_ERROR);
//        SignedRawTransaction settlementRawTx = this.signVerify(param.getSettlement().getAddress(),param.getSettlement().getSignature(),BizCodeEnum.LAYER2_SIGN_ERROR);
        //todo check data is valid

        return zkSync2TxBiz.settlement(param);
    }

    @SneakyThrows
    private SignedRawTransaction signVerify(String address, String sign, BizCodeEnum bizCodeEnum) {
        SignedRawTransaction signedRawTransaction;

        try {
            signedRawTransaction = (SignedRawTransaction) TransactionDecoder.decode(sign);
        } catch (Exception e) {
            //  signature error
            throw new BizException(bizCodeEnum);
        }

        // verify signature identity
        if (!address.equals(signedRawTransaction.getFrom())) {
            //  signature error
            throw new BizException(bizCodeEnum);
        }
        return signedRawTransaction;
    }
}
