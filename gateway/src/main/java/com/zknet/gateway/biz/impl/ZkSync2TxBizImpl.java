package com.zknet.gateway.biz.impl;

import cn.hutool.json.JSONUtil;
import com.zknet.engine.constants.*;
import com.zknet.engine.entity.*;
import com.zknet.engine.service.*;
import com.zknet.gateway.biz.TxBiz;
import com.zknet.gateway.config.RequestHeaderContext;
import com.zknet.gateway.exception.BizCodeEnum;
import com.zknet.gateway.exception.BizException;
import com.zknet.gateway.jsonrpc.dto.DepositDto;
import com.zknet.gateway.jsonrpc.dto.ZkSync2DepositDto;
import com.zknet.gateway.jsonrpc.dto.WithdrawDto;
import com.zknet.gateway.jsonrpc.dto.ZkSync2WithdrawDto;
import com.zknet.gateway.jsonrpc.param.ApiKeyQueryParam;
import com.zknet.gateway.jsonrpc.param.BindAccountParam;
import com.zknet.gateway.jsonrpc.param.SettlementParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service("zkSync2TxBiz")
public class ZkSync2TxBizImpl implements TxBiz {

    @Resource
    private Web3Service defaultWeb3Service;

    @Resource
    private ZkNetEventService zkNetEventService;

    @Resource
    private ZknetExOrderService zknetExOrderService;

    @Resource
    private ZknetExSettlementService zknetExSettlementService;

    @Resource
    private ZkNetL2TokenService zkNetL2TokenService;

    @Resource
    private ZknetBusinessContractService zknetBusinessContractService;

    @Resource
    private ZknetExchangeService zknetExchangeService;

    @Override
    public TxStatusEnum getTxStatus(String hash) throws IOException {
        if (!StringUtils.hasText(hash)) {
            throw new BizException(BizCodeEnum.MISSING_REQUIRED_FIELDS);
        }
        return defaultWeb3Service.getTxStatus(hash, PlatformEnum.ZKSYNC2);
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
        return defaultWeb3Service.sendRawTx(hexValue, PlatformEnum.ZKSYNC2);
    }

    @SneakyThrows
    @Override
    public String deposit(DepositDto dto) {
        String txHashLocal = Hash.sha3(((ZkSync2DepositDto) dto).getSign());
        ZkNetEvent querySign = zkNetEventService.findByTxHash(txHashLocal);
        if (querySign != null) {
            throw new BizException(BizCodeEnum.SIGN_REPEAT_SUBMIT);
        }

        //  broadcast transaction information
        EthSendTransaction ethSendTransaction = this.broadcast(((ZkSync2DepositDto) dto).getSign());

        ZkNetEvent zkNetEvent = new ZkNetEvent();

        zkNetEvent.setTxStatus(TxStatusEnum.PENDING);

        if (ethSendTransaction.getTransactionHash() == null || !txHashLocal.equals(ethSendTransaction.getTransactionHash())) {
            zkNetEvent.setTxStatus(TxStatusEnum.FAIL);
            log.error("broadcast sign {} error reason {}", ((ZkSync2DepositDto) dto).getSign(), JSONUtil.parse(ethSendTransaction).toStringPretty());
//            zkNetEvent.setErrorInfo(JSONUtil.parse(ethSendTransaction.getError()).toStringPretty());
        }

        zkNetEvent.setType(TransactionFlowTypeEnum.L2);
        zkNetEvent.setMode(ContractMethodEnum.DEPOSIT.getMethod());
        zkNetEvent.setTxSign(((ZkSync2DepositDto) dto).getSign());
        zkNetEvent.setTxHash(txHashLocal);

        // sava data
        zkNetEventService.save(zkNetEvent);
        return txHashLocal;
    }

    /**
     * withdraw
     *
     * @param withdrawDto
     * @return
     */
    @SneakyThrows
    @Override
    public String withdraw(WithdrawDto withdrawDto) {
        String txHashLocal = Hash.sha3(((ZkSync2WithdrawDto) withdrawDto).getSign());
        ZkNetEvent querySign = zkNetEventService.findByTxHash(txHashLocal);
        if (querySign != null) {
            throw new BizException(BizCodeEnum.SIGN_REPEAT_SUBMIT);
        }

        ZkNetEvent zkNetEvent = new ZkNetEvent();

        //  broadcast transaction information
        EthSendTransaction ethSendTransaction = this.broadcast(((ZkSync2WithdrawDto) withdrawDto).getSign());

        zkNetEvent.setTxStatus(TxStatusEnum.PENDING);

        if (ethSendTransaction.getTransactionHash() == null || !txHashLocal.equals(ethSendTransaction.getTransactionHash())) {
            zkNetEvent.setTxStatus(TxStatusEnum.FAIL);
            log.error("broadcast sign {} error reason {}", ((ZkSync2WithdrawDto) withdrawDto).getSign(), JSONUtil.parse(ethSendTransaction).toStringPretty());
//            zkNetEvent.setErrorInfo(JSONUtil.parse(ethSendTransaction.getError()).toStringPretty());
        }

        zkNetEvent.setType(TransactionFlowTypeEnum.L2);
        zkNetEvent.setMode(ContractMethodEnum.WITHDRAW.getMethod());
        zkNetEvent.setTxSign(((ZkSync2WithdrawDto) withdrawDto).getSign());
        zkNetEvent.setTxHash(txHashLocal);

        // sava data
        zkNetEventService.save(zkNetEvent);

        return txHashLocal;
    }

    @SneakyThrows
    @Override
    public String bindAccount(BindAccountParam param) {
        String txHashLocal = Hash.sha3(param.getSign2());
        ZkNetEvent querySign = zkNetEventService.findByTxHash(txHashLocal);
        if (querySign != null) {
            throw new BizException(BizCodeEnum.SIGN_REPEAT_SUBMIT);
        }

        ZkNetEvent zkNetEvent = new ZkNetEvent();

        //  broadcast transaction information
        EthSendTransaction ethSendTransaction = this.broadcast(param.getSign2());

        zkNetEvent.setTxStatus(TxStatusEnum.PENDING);

        if (ethSendTransaction.getTransactionHash() == null || !txHashLocal.equals(ethSendTransaction.getTransactionHash())) {
            zkNetEvent.setTxStatus(TxStatusEnum.FAIL);
            log.error("broadcast sign {} error reason {}", param.getSign2(), JSONUtil.parse(ethSendTransaction).toStringPretty());
//            zkNetEvent.setErrorInfo(JSONUtil.parse(ethSendTransaction.getError()).toStringPretty());
        }

        zkNetEvent.setType(TransactionFlowTypeEnum.L2);
        zkNetEvent.setMode(ContractMethodEnum.BIND_ACCOUNT.getMethod());
        zkNetEvent.setTxSign(param.getSign2());
        zkNetEvent.setTxHash(txHashLocal);

        // sava data
        zkNetEventService.save(zkNetEvent);

        return txHashLocal;
    }


    @SneakyThrows
    @Override
    public String settlement(SettlementParam param) {
        //todo lock orderA and orderB
        String exNo = RequestHeaderContext.getInstance().getExNo();
        if (zknetExSettlementService.getRecordByExNoAndStNo(exNo,param.getSettlement().getStNo()) != null) {
            throw new BizException(BizCodeEnum.SIGN_REPEAT_SUBMIT);
        }
        List<Type> inputParams = generateL2SettlementParams(param);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        ZknetBusinessContract zknetBusinessContract = zknetBusinessContractService.getByExNoAndPlatform(exNo,PlatformEnum.ZKSYNC2);
        String contractAddress = zknetBusinessContract.getContractAddress();
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx(ContractMethodEnum.SETTLEMENT.getMethod(),inputParams,outputParameters,BigInteger.ZERO,contractAddress,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            log.warn("settlement fail,exNo:{},error:{}",exNo,JSONUtil.toJsonStr(ethSendTransaction.getError()));
            throw new BizException(BizCodeEnum.TRANSACTION_FAIL,ethSendTransaction.getError().getMessage());
        } else
            log.info("settlement hash:{}",ethSendTransaction.getTransactionHash());
        ZknetExOrder orderA = buildOrder(param.getPartA(),exNo);
        ZknetExOrder orderB = buildOrder(param.getPartB(),exNo);
        ZknetExSettlement settlement = buildSettlement(param,exNo,ethSendTransaction);
        try {
            zknetExSettlementService.saveSettlementAndOrder(settlement,orderA,orderB);
        } catch (Exception e) {
            //todo record fail and handle later
            log.error("settlement save db error,param:{},error:",param,e);
        }

        return ethSendTransaction.getTransactionHash();
    }

    private List<Type> generateL2SettlementParams(SettlementParam param) {
        SettlementParam.Part partA = param.getPartA();
        SettlementParam.Part partB = param.getPartB();
        SettlementParam.Settlement settlementParam = param.getSettlement();
        DynamicStruct orderA = generateOrder(partA);
        DynamicStruct orderB = generateOrder(partB);
        DynamicStruct settlementInfo = new DynamicStruct(new Uint256(new BigInteger(settlementParam.getPositionSold())),
                new Uint256(new BigInteger(settlementParam.getPartAFee())),
                new Uint256(new BigInteger(settlementParam.getPartBFee())));
        return Arrays.asList(orderA,orderB,settlementInfo);
    }

    private DynamicStruct generateOrder(SettlementParam.Part part) {
        return new DynamicStruct(new Uint256(new BigInteger(part.getId())),
                new Address(part.getTrader()),
                new Uint64(new BigInteger(part.getPositionId())),
                new Uint32(new BigInteger(part.getPositionToken())),
                new Int256(new BigInteger(part.getPositionAmount())),
                new Uint256(new BigInteger(part.getFee())),
                new Uint32(new BigInteger(part.getTimestamp())),
                new DynamicBytes(part.getSignature().getBytes())
        );
    }

    @Override
    public String getApiKey(ApiKeyQueryParam param) {
        ZknetExchange zknetExchange = zknetExchangeService.getByAddress(param.getFrom());
        if (zknetExchange == null) {
            throw new BizException(BizCodeEnum.EXCHANGE_HAS_NO_INFO_RECORD);
        }
        return zknetExchange.getApiKey();
    }

    @Override
    public String getExNoByApiKey(String apiKey) {
        return zknetExchangeService.getExNoByApiKey(apiKey);
    }

    @Override
    public String registerToken(String address, BigInteger tokenId) throws ExecutionException, InterruptedException {
        Address tokenAddress = new Address(address);
        Uint32 tokenId_ = new Uint32(tokenId);
        String exNo = RequestHeaderContext.getInstance().getExNo();
        List<Type> inputParams = Arrays.asList(tokenAddress,tokenId_);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        ZknetBusinessContract zknetBusinessContract = zknetBusinessContractService.getByExNoAndPlatform(exNo,PlatformEnum.ZKSYNC2);
        String contractAddress = zknetBusinessContract.getContractAddress();
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("registerToken",inputParams,outputParameters,BigInteger.ZERO,contractAddress,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("registerToken fail,exNo:{},error:{}",exNo,JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
        log.info("registerToken hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    private String getPerpetualContractAddress() {
        String exNo = RequestHeaderContext.getInstance().getExNo();
        ZknetBusinessContract zknetBusinessContract = zknetBusinessContractService.getByExNoAndPlatform(exNo,PlatformEnum.ZKSYNC2);
        String contractAddress = zknetBusinessContract.getContractAddress();
        return contractAddress;
    }

    @Override
    public String erc20Transfer(String tokenAddress, String to, BigInteger value) throws ExecutionException, InterruptedException {
        Address theTo = new Address(to);
        Uint256 amount = new Uint256(value);
        List<Type> inputParams = Arrays.asList(theTo,amount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("transfer",inputParams,outputParameters,BigInteger.ZERO,tokenAddress,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("erc20Transfer fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("erc20Transfer hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String adminApprove(String tokenAddress, String spender, BigInteger value) throws ExecutionException, InterruptedException {
        Address spenderAddress = new Address(getPerpetualContractAddress());
        Uint256 amount = new Uint256(value);
        List<Type> inputParams = Arrays.asList(spenderAddress,amount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("approve",inputParams,outputParameters,BigInteger.ZERO,tokenAddress,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("adminApprove fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
        log.info("adminApprove hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String adminDeposit(BigInteger tokenId, BigInteger amount) throws ExecutionException, InterruptedException {
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);
        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("deposit",inputParams,outputParameters,BigInteger.ZERO,contract,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("adminDeposit fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
        log.info("adminDeposit hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String adminWithdraw(BigInteger tokenId, BigInteger amount) throws ExecutionException, InterruptedException {
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);
        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("withdraw",inputParams,outputParameters,BigInteger.ZERO,contract,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("adminWithdraw fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("adminWithdraw hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String internalWithdraw(BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException {
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);
        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("withdraw",inputParams,outputParameters,BigInteger.ZERO,contract,privateKey,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("internalWithdraw fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("internalWithdraw hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String adminTransfer(String to, BigInteger tokenId, BigInteger amount, BigInteger fee) throws ExecutionException, InterruptedException {
        Address theTo = new Address(to);
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);
        Uint256 theFee = new Uint256(fee);

        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(theTo,theTokenId,theAmount,theFee);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("transfer",inputParams,outputParameters,BigInteger.ZERO,contract,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("adminTransfer fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("adminTransfer hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String adminPositionDeposit(BigInteger positionId, BigInteger tokenId, BigInteger amount) throws ExecutionException, InterruptedException {
        Uint64 thePositionId = new Uint64(positionId);
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);

        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(thePositionId,theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("positionDeposit",inputParams,outputParameters,BigInteger.ZERO,contract,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("adminPositionDeposit fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("adminPositionDeposit hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String internalPositionDeposit(BigInteger positionId, BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException {
        Uint64 thePositionId = new Uint64(positionId);
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);

        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(thePositionId,theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("positionDeposit",inputParams,outputParameters,BigInteger.ZERO,contract,privateKey,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("internalPositionDeposit fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("internalPositionDeposit hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String adminPositionWithdraw(BigInteger positionId, BigInteger tokenId, BigInteger amount) throws ExecutionException, InterruptedException {
        Uint64 thePositionId = new Uint64(positionId);
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);

        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(thePositionId,theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("positionWithdraw",inputParams,outputParameters,BigInteger.ZERO,contract,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("adminPositionWithdraw fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("adminPositionWithdraw hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    @Override
    public String internalPositionWithdraw(BigInteger positionId, BigInteger tokenId, BigInteger amount, String privateKey) throws ExecutionException, InterruptedException {
        Uint64 thePositionId = new Uint64(positionId);
        Uint32 theTokenId = new Uint32(tokenId);
        Uint256 theAmount = new Uint256(amount);

        String contract = getPerpetualContractAddress();
        List<Type> inputParams = Arrays.asList(thePositionId,theTokenId,theAmount);
        List<TypeReference<?>> outputParameters = Collections.singletonList(new TypeReference<Type>() {});
        EthSendTransaction ethSendTransaction = defaultWeb3Service.generateAndSendContractRawTx("positionWithdraw",inputParams,outputParameters,BigInteger.ZERO,contract,privateKey,PlatformEnum.ZKSYNC2);
        if (ethSendTransaction.getError() != null) {
            // todo fail
            log.warn("internalPositionWithdraw fail,error:{}",JSONUtil.toJsonStr(ethSendTransaction.getError()));
        } else
            log.info("internalPositionWithdraw hash:{}",ethSendTransaction.getTransactionHash());
        return ethSendTransaction.getTransactionHash();
    }

    private ZknetExSettlement buildSettlement(SettlementParam param, String exNo, EthSendTransaction ethSendTransaction) {
        return ZknetExSettlement.builder()
                .exNo(exNo)
                .orderAFee(new BigInteger(param.getSettlement().getPartAFee()))
                .orderANo(param.getPartA().getId())
                .orderBFee(new BigInteger(param.getSettlement().getPartBFee()))
                .orderBNo(param.getPartB().getId())
                .status(SettlementStatusEnum.PENDING)
                .stNo(param.getSettlement().getStNo())
                .txHash(ethSendTransaction.getTransactionHash()).build();
    }

    private ZknetExOrder buildOrder(SettlementParam.Part part, String exNo) {
        ZknetExOrder order = ZknetExOrder.builder()
                .orderNo(part.getId())
                .exNo(exNo)
                .extend(part.getExtend())
                .fee(part.getFee())
                .orderType(OrderTypeEnum.getByName(part.getType()))
                .positionId(part.getPositionId())
                .sign(part.getSignature())
                .status(OrderStatusEnum.UNFINISHED)
                .timestamp(Integer.valueOf(part.getTimestamp()))
                .userAddr(part.getTrader())
                .positionToken(part.getPositionToken())
                .positionAmount(part.getPositionAmount())
                .build();
        return order;
    }

    private String getL2ContractAddress(String exNo) {
        ZknetBusinessContract zknetBusinessContract = zknetBusinessContractService.getByExNoAndPlatform(exNo, PlatformEnum.ZKSYNC2);
        if (zknetBusinessContract == null) {
            throw new BizException(BizCodeEnum.EXCHANGE_HAS_NO_CONTRACT_RECORD);
        }
        return zknetBusinessContract.getContractAddress();
    }

}
