package com.zknet.gateway.biz.impl;

import com.zknet.engine.constants.*;
import com.zknet.engine.service.Web3Service;
import com.zknet.engine.service.ZkNetEventService;
import com.zknet.gateway.biz.TxBiz;
import com.zknet.gateway.jsonrpc.dto.DepositDto;
import com.zknet.gateway.jsonrpc.dto.WithdrawDto;
import com.zknet.gateway.jsonrpc.param.BindAccountParam;
import com.zknet.gateway.jsonrpc.param.ApiKeyQueryParam;
import com.zknet.gateway.jsonrpc.param.SettlementParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service("ethTxBiz")
public class EthTxBizImpl implements TxBiz {

    @Resource
    private Web3Service defaultWeb3Service;

    @Override
    public TxStatusEnum getTxStatus(String hash) throws IOException {
        return defaultWeb3Service.getTxStatus(hash, PlatformEnum.ETH);
    }

    /**
     * broadcast transaction
     *
     * @param hexValue
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public EthSendTransaction broadcast(String hexValue) throws ExecutionException, InterruptedException {
        return defaultWeb3Service.sendRawTx(hexValue, PlatformEnum.ETH);
    }

    @Override
    public String bindAccount(BindAccountParam param) {
        return null;
    }

    @SneakyThrows
    @Override
    public String deposit(DepositDto dto) {

        return null;
    }

    /**
     * withdraw
     *
     * @param withdrawDto
     * @return
     */
    @Override
    public String withdraw(WithdrawDto withdrawDto) {
        return null;
    }

    @Override
    public String settlement(SettlementParam param) {
        return null;
    }

    @Override
    public String getApiKey(ApiKeyQueryParam param) {
        return null;
    }

    @Override
    public String getExNoByApiKey(String apiKey) {
        return null;
    }

    @Override
    public String registerToken(String address, BigInteger tokenId) {
        return null;
    }

    @Override
    public String erc20Transfer(String tokenAddress, String to, BigInteger value) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public String adminApprove(String tokenAddress, String spender, BigInteger value) {
        return null;
    }

    @Override
    public String adminDeposit(BigInteger tokenId, BigInteger amount) {
        return null;
    }

    @Override
    public String adminWithdraw(BigInteger tokenId, BigInteger amount) {
        return null;
    }

    @Override
    public String internalWithdraw(BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public String adminTransfer(String to, BigInteger tokenId, BigInteger amount, BigInteger fee) {
        return null;
    }

    @Override
    public String adminPositionDeposit(BigInteger positionId, BigInteger tokenId, BigInteger amount) {
        return null;
    }

    @Override
    public String internalPositionDeposit(BigInteger positionId, BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public String adminPositionWithdraw(BigInteger positionId, BigInteger tokenId, BigInteger amount) {
        return null;
    }

    @Override
    public String internalPositionWithdraw(BigInteger positionId, BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException {
        return null;
    }

}
