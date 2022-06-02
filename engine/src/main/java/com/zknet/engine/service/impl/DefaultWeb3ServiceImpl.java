package com.zknet.engine.service.impl;

import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.constants.TxStatusEnum;
import com.zknet.engine.service.Web3Service;
import com.zknet.engine.service.ZkNetL2TokenService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import io.reactivex.functions.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service("defaultWeb3Service")
public class DefaultWeb3ServiceImpl implements Web3Service {

    @Value("${web3.zksync2.url}")
    private String zkSync2Url;
    @Value("${web3.eth.url}")
    private String ethUrl;
    @Value("${web3.zksync2.adminKey}")
    private String zkSync2AdminPrivateKey;
    @Value("${web3.eth.adminKey}")
    private String ethAdminPrivateKey;
    @Value("${web3.zksync2.chainId}")
    private long chainId;
    @Value("${web3.zksync2.chainId}")
    private long zkSync2ChainId;
    @Value("${web3.eth.chainId}")
    private long ethChainId;

    private Web3j zkSync2Web3j;

    private Web3j ethWeb3j;

    @Resource
    private ZkNetL2TokenService zkNetL2TokenService;

    @Override
    public EthSendTransaction sendRawTx(String rawTxHex, PlatformEnum platform) throws ExecutionException, InterruptedException {
        //todo check param
        EthSendTransaction ethSendTransaction = getWeb3jByPlatform(platform).ethSendRawTransaction(rawTxHex).sendAsync().get();

//        log.info(" broadcast result:{}", JSONUtil.parse(ethSendTransaction).toStringPretty());
//        String hash = ethSendTransaction == null ? null : ethSendTransaction.getTransactionHash();
        return ethSendTransaction;
    }

    @SneakyThrows
    @Override
    public EthSendTransaction generateAndSendContractRawTx(String name, List<Type> inputParameters, List<TypeReference<?>> outputParameters, BigInteger value, String contractAddress, PlatformEnum platform) throws ExecutionException, InterruptedException {
        String privateKey = getPrivateKeyByPlatform(platform);
        EthSendTransaction ethSendTransaction = generateAndSendContractRawTx(name,inputParameters,outputParameters,value,contractAddress,privateKey,platform);
        return ethSendTransaction;
    }
    @SneakyThrows
    @Override
    public EthSendTransaction generateAndSendContractRawTx(String name, List<Type> inputParameters, List<TypeReference<?>> outputParameters, BigInteger value, String contractAddress, String privateKey, PlatformEnum platform) throws ExecutionException, InterruptedException {
        Web3j web3j = getWeb3jByPlatform(platform);
        long chainId = getChainIdByPlatform(platform);
        Credentials adminAccount = Credentials.create(privateKey);
        String address = adminAccount.getAddress();
        BigInteger nonce = web3j.ethGetTransactionCount(address,DefaultBlockParameterName.PENDING).send().getTransactionCount();
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(3000000L);
        Function function = new Function(name,inputParameters, outputParameters);
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                contractAddress, value, encodedFunction);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction,chainId,adminAccount);
        String hexValue = Numeric.toHexString(signMessage);
        String txHashLocal = Hash.sha3(hexValue);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        if (!StringUtils.hasText(ethSendTransaction.getResult())) {
            ethSendTransaction.setResult(txHashLocal);
        }
        return ethSendTransaction;
    }

    @Override
    public TxStatusEnum getTxStatus(String hash, PlatformEnum platform) throws IOException {
        TransactionReceipt receipt = getWeb3jByPlatform(platform).ethGetTransactionReceipt(hash).send().getResult();
        return resolveStatusFromTxReceipt(receipt);
    }

    @Override
    public void subscribeLogEventFlowable(EthFilter filter, Consumer<Log> consumer, PlatformEnum platform) {
        getWeb3jByPlatform(platform).ethLogFlowable(filter).subscribe(consumer);
    }

    @PostConstruct
    private void init() {
        zkSync2Web3j = Web3j.build(new HttpService(zkSync2Url));
        ethWeb3j = Web3j.build(new HttpService(ethUrl));
    }

    private Web3j getWeb3jByPlatform(PlatformEnum platform) {
        switch (platform) {
            case ZKSYNC2:
                return zkSync2Web3j;
            default:
                return ethWeb3j;
        }
    }

    private String getPrivateKeyByPlatform(PlatformEnum platform) {
        switch (platform) {
            case ZKSYNC2:
                return zkSync2AdminPrivateKey;
            default:
                return ethAdminPrivateKey;
        }
    }
    private long getChainIdByPlatform(PlatformEnum platform) {
        switch (platform) {
            case ZKSYNC2:
                return zkSync2ChainId;
            default:
                return ethChainId;
        }
    }

    @SneakyThrows
    @Override
    public long getErc20TokenOrETHBalance(String tokenName, String address, PlatformEnum platform) {
        if (platform == PlatformEnum.ETH && "ETH".equals(tokenName)) {
            return getETHCoinBalance(address);
        }
        return getERC20Balance(address,zkNetL2TokenService.getTokenAddress(tokenName),"balanceOf",Arrays.asList(new Address(address)),getWeb3jByPlatform(platform));
    }

    @SneakyThrows
    @Override
    public long getBalanceFromBusinessContract(String userAddress, String tokenName, String contractAddress, PlatformEnum platform) {
        return getERC20Balance(userAddress,contractAddress,"balanceOf",Arrays.asList(new Address(userAddress),new Address(zkNetL2TokenService.getTokenAddress(tokenName))),getWeb3jByPlatform(platform));
    }

    private long getETHCoinBalance(String address) throws IOException {
        BigInteger bal = ethWeb3j.ethGetBalance(address, DefaultBlockParameterName.PENDING).send().getBalance();
        return bal.longValue();
    }

    private long getERC20Balance(String address, String contractAddress, String methodName, List<Type> inputParameters, Web3j web3j) throws ExecutionException, InterruptedException {
        Function function = new Function(methodName,
                inputParameters,
                Arrays.asList(new TypeReference<Address>() {
                }));

        String encode = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, contractAddress, encode);
        EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.PENDING).sendAsync().get();
        String value = ethCall.getResult();
        return new BigInteger(value.substring(2),16).longValue();
    }

}
