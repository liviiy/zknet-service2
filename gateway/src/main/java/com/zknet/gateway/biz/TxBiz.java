package com.zknet.gateway.biz;

import com.zknet.engine.constants.TxStatusEnum;
import com.zknet.gateway.jsonrpc.dto.DepositDto;
import com.zknet.gateway.jsonrpc.dto.WithdrawDto;
import com.zknet.gateway.jsonrpc.dto.ZkSync2WithdrawDto;
import com.zknet.gateway.jsonrpc.param.BindAccountParam;
import com.zknet.gateway.jsonrpc.param.ApiKeyQueryParam;
import com.zknet.gateway.jsonrpc.param.DepositParam;
import com.zknet.gateway.jsonrpc.param.SettlementParam;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface TxBiz {
    /**
     * get a transaction status
     *
     * @param hash
     * @return
     * @throws IOException
     */
    TxStatusEnum getTxStatus(String hash) throws IOException;

    /**
     * broadcast transaction
     *
     * @param hexValue
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    EthSendTransaction broadcast(String hexValue) throws ExecutionException, InterruptedException;

    String bindAccount(BindAccountParam param);

    /**
     * deposit
     *
     * @param dto
     * @return
     */
    String deposit(DepositDto dto) ;

    /**
     * withdraw
     *
     * @param withdrawDto
     * @return
     */
    String withdraw(WithdrawDto withdrawDto);

    /**
     * settlement
     * @param param
     * @return
     */
    String settlement(SettlementParam param);

    String getApiKey(ApiKeyQueryParam param);

    String getExNoByApiKey(String apiKey);

    String registerToken(String address, BigInteger tokenId) throws ExecutionException, InterruptedException;

    String erc20Transfer(String tokenAddress, String to, BigInteger value) throws ExecutionException, InterruptedException;

    /**
     * admin approve spender,in this scene the spender is perpetual
     * @param tokenAddress
     * @param spender
     * @param value
     * @return
     */
    String adminApprove(String tokenAddress,String spender,BigInteger value) throws ExecutionException, InterruptedException;

    /**
     * admin deposit his token into his perpetual balance
     * @param tokenId
     * @param amount
     * @return
     */
    String adminDeposit(BigInteger tokenId,BigInteger amount) throws ExecutionException, InterruptedException;

    /**
     * admin withdraw his perpetual balance to his token balance
     * @param tokenId
     * @param amount
     * @return
     */
    String adminWithdraw(BigInteger tokenId,BigInteger amount) throws ExecutionException, InterruptedException;

    String internalWithdraw(BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException;

    /**
     * admin transfer his perpetual balance to to's perpetual balance
     * @param to
     * @param tokenId
     * @param amount
     * @param fee
     * @return
     */
    String adminTransfer(String to, BigInteger tokenId,BigInteger amount, BigInteger fee) throws ExecutionException, InterruptedException;

    /**
     * admin deposit his perpetual balance into his position balance
     * @param positionId
     * @param tokenId
     * @param amount
     * @return
     */
    String adminPositionDeposit(BigInteger positionId, BigInteger tokenId, BigInteger amount) throws ExecutionException, InterruptedException;

    String internalPositionDeposit(BigInteger positionId, BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException;

    /**
     * admin withdraw his position balance into his perpetual balance
     * @param positionId
     * @param tokenId
     * @param amount
     * @return
     */
    String adminPositionWithdraw(BigInteger positionId, BigInteger tokenId, BigInteger amount) throws ExecutionException, InterruptedException;

    String internalPositionWithdraw(BigInteger positionId, BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException;
}
