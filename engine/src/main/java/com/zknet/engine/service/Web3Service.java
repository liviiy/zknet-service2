package com.zknet.engine.service;

import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.constants.TxStatusEnum;
import io.reactivex.functions.Consumer;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Web3Service {
    /**
     * send a raw transaction
     * @param rawTxHex signed transaction,0x prefix hex format
     * @return the tx hash
     */
    EthSendTransaction sendRawTx(String rawTxHex, PlatformEnum platform) throws ExecutionException, InterruptedException;

    /**
     *
     * @param name
     * @param inputParameters
     * @param outputParameters
     * @param value
     * @param contractAddress
     * @param platform
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    EthSendTransaction generateAndSendContractRawTx(String name, List<Type> inputParameters, List<TypeReference<?>> outputParameters, BigInteger value, String contractAddress, PlatformEnum platform) throws ExecutionException, InterruptedException;

    @SneakyThrows
    EthSendTransaction generateAndSendContractRawTx(String name, List<Type> inputParameters, List<TypeReference<?>> outputParameters, BigInteger value, String contractAddress, String privateKey, PlatformEnum platform) throws ExecutionException, InterruptedException;

    /**
     * get a transaction status
     * @param hash 0x prefix hex format
     * @return TxStatusEnum
     */
    TxStatusEnum getTxStatus(String hash, PlatformEnum platform) throws IOException;

    /**
     * subscribe Log Event by rxjava
     * @param filter
     * @param consumer
     */
    void subscribeLogEventFlowable(EthFilter filter, Consumer<Log> consumer, PlatformEnum platform);

    default TxStatusEnum resolveStatusFromTxReceipt(TransactionReceipt receipt) {
        if (receipt == null) {
            return TxStatusEnum.NO_DATA;
        }
        String status = receipt.getStatus();
        if (!StringUtils.hasText(status)) {
            return TxStatusEnum.PENDING;
        }
        if (status.equals("0x1") && !CollectionUtils.isEmpty(receipt.getLogs())) {
            return TxStatusEnum.SUCCESS;
        }
        return TxStatusEnum.FAIL;
    }

    long getErc20TokenOrETHBalance(String tokenName, String address, PlatformEnum platform);

    @SneakyThrows
    long getBalanceFromBusinessContract(String userAddress, String tokenName, String contractAddress, PlatformEnum platform);
}
