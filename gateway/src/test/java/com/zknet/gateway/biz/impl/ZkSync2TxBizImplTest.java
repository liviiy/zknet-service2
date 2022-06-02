package com.zknet.gateway.biz.impl;

import com.zknet.gateway.biz.TxBiz;
import com.zknet.gateway.config.RequestHeaderContext;
import com.zknet.gateway.jsonrpc.param.SettlementParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class ZkSync2TxBizImplTest {

    public static String ETH_ADDRESS = "0xEeeeeEeeeEeEeeEeEeEeeEEEeeeeEeeeeeeeEEeE";
    public static String ERC20_ADDRESS = "0x3fad2b2e21ea1c96618cc76a42fb5a77c3f71c6f";

    //admin account 8
    public static String adminPrivateKey = "0x7726827caac94a7f9e1b160f7ea819f172f7b6f9d2a97f992c38edeab82d4110";
    public static String adminAddress = "0x36615Cf349d7F6344891B1e7CA7C72883F5dc049";
    //account 3
    public static String accountAPrivateKey = "0x2533b79bdc7f04bcbe4458df1e0971e143a588a7ac742b7a6a7ff0b1fa34e5e9";
    public static String accountAAddress = "0x91CF51fe36d03426aD4ddd2F41f6f6D387585F6E";
    //account 7
    public static String accountBPrivateKey = "0x2604475f516fabd2c58487ba143893232198b6643f35a064e016948c36c4c085";
    public static String accountBAddress = "0xaB2801adCF1cDd4fDAA51189bC595685271a267E";
    // the perpetual contract Address
    public static String perpetualAddress = "0xb8202257dEC64CDAd41ab550C1330D6FFf6b80BD";

    public static BigInteger ETH_ID = BigInteger.ZERO;
    public static BigInteger ERC20_ID = BigInteger.ONE;

    public static BigInteger positionId1 = BigInteger.ONE;
    public static BigInteger positionId2 = BigInteger.valueOf(2L);


    @Autowired
    private TxBiz zkSync2TxBiz;

    @BeforeEach
    public void init() {
        new RequestHeaderContext.RequestHeaderContextBuild().exNo("ex001","333").bulid();
    }


    @Test
    void registerToken() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.registerToken(ETH_ADDRESS, ETH_ID);
        zkSync2TxBiz.registerToken(ERC20_ADDRESS,ERC20_ID);
    }

    @Test
    void adminApprove() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.adminApprove(ETH_ADDRESS,"",BigInteger.valueOf(100L));
        zkSync2TxBiz.adminApprove(ERC20_ADDRESS,"",BigInteger.valueOf(10L));
    }

    @Test
    void adminDeposit() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.adminDeposit(ETH_ID, BigInteger.valueOf(100L));
        zkSync2TxBiz.adminDeposit(ERC20_ID, BigInteger.valueOf(10L));
    }

    @Test
    void adminTransfer() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.adminTransfer(accountAAddress,ERC20_ID,BigInteger.valueOf(5L),BigInteger.ZERO);
    }

    @Test
    void erc20Transfer() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.erc20Transfer(ETH_ADDRESS,accountAAddress, Convert.toWei("100", Convert.Unit.ETHER).toBigInteger());
    }

    @Test
    void adminPositionDeposit() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.adminPositionDeposit(positionId1,ERC20_ID,BigInteger.valueOf(5L));
        zkSync2TxBiz.internalPositionDeposit(positionId2,ERC20_ID,BigInteger.valueOf(5L),accountAPrivateKey);
    }

    @Test
    void settlement() {
        SettlementParam.Part orderA = SettlementParam.Part.builder().id("6").trader(adminAddress).positionId(positionId1.toString()).positionToken(ERC20_ID.toString()).positionAmount("100").fee("1").timestamp("1653622572").signature("").build();
        SettlementParam.Part orderB = SettlementParam.Part.builder().id("7").trader(accountAAddress).positionId(positionId2.toString()).positionToken(ERC20_ID.toString()).positionAmount("-100").fee("1").timestamp("1653622572").signature("").build();
        SettlementParam.Settlement settlement = SettlementParam.Settlement.builder().stNo("8").positionSold("10").partAFee("1").partBFee("1").build();
        SettlementParam param = SettlementParam.builder().partA(orderA).partB(orderB).settlement(settlement).build();
        zkSync2TxBiz.settlement(param);
    }

    @Test
    void adminPositionWithdraw() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.adminPositionWithdraw(positionId1,ERC20_ID,BigInteger.valueOf(5L));
        zkSync2TxBiz.internalPositionWithdraw(positionId2,ERC20_ID,BigInteger.valueOf(5L),accountAPrivateKey);
    }

    @Test
    void adminWithdraw() throws ExecutionException, InterruptedException {
        zkSync2TxBiz.adminWithdraw(ERC20_ID,BigInteger.valueOf(5L));
        zkSync2TxBiz.internalWithdraw(ERC20_ID,BigInteger.valueOf(5L),accountAPrivateKey);
    }

    @Test
    void getTxStatusByHash() throws IOException {
        log.info("adminApprove status:{}",zkSync2TxBiz.getTxStatus("0x624896e880985aef143b2244505f9e318fdf6339b113230931d276f039bf9e5b"));
        log.info("adminDeposit status:{}",zkSync2TxBiz.getTxStatus("0xcad782c9b55c3e3493553fc78afd479720233859be8e93bfd4290787c9a1a3b7"));
        log.info("adminTransfer status:{}",zkSync2TxBiz.getTxStatus("0x75e4e24a3406dee004904c9489350072d7cd623ec6fb982e9316a837a07df8f2"));
        log.info("adminPositionDeposit status:{}",zkSync2TxBiz.getTxStatus("0xc033cb1b6227ff959a59e0c928cd9aa24d74b5473836d76cba0fe2f22eaad83c"));
        log.info("internalPositionDeposit status:{}",zkSync2TxBiz.getTxStatus("0x78255587b70856e93c0720d5e92871ec04521f82a7c0d881b3e51bb9b4a39d4f"));
        log.info("settlement status:{}",zkSync2TxBiz.getTxStatus("0xb48f632763db57932f883faaae34033828f933b6a406959e4f3f16c187dd04f9"));
        log.info("adminPositionWithdraw status:{}",zkSync2TxBiz.getTxStatus("0x186a57d1c404b49c0a9a9a7aad6a136982897b14bb279d2b693ff28313112112"));
        log.info("internalPositionWithdraw status:{}",zkSync2TxBiz.getTxStatus("0x175eebcf6e39650ddbd2c83b92f0f49a41afac1066df0cdbd58638c0df5c1b99"));
        log.info("adminWithdraw status:{}",zkSync2TxBiz.getTxStatus("0xda8285fc9f5f15e3e9e503817fc842283155f84b5848b781b2f2e10985d50caa"));
        log.info("internalWithdraw status:{}",zkSync2TxBiz.getTxStatus("0xec7402852c0f861505b6831592dcf5b3c8f748661931af363305f378ad34ec48"));
    }

    @Test
    void fullFlowTest() throws ExecutionException, InterruptedException {

        //approve
        zkSync2TxBiz.adminApprove(ERC20_ADDRESS,"",BigInteger.valueOf(10L));

        //deposit into perpetual contract
        zkSync2TxBiz.adminDeposit(ERC20_ID, BigInteger.valueOf(10L));

        //transfer perpetual balance from admin to accountA
        zkSync2TxBiz.adminTransfer(accountAAddress,ERC20_ID,BigInteger.valueOf(5L),BigInteger.ZERO);

        //deposit position
        zkSync2TxBiz.adminPositionDeposit(positionId1,ERC20_ID,BigInteger.valueOf(5L));
        zkSync2TxBiz.internalPositionDeposit(positionId2,ERC20_ID,BigInteger.valueOf(5L),accountAPrivateKey);

        //settlement
        SettlementParam.Part orderA = SettlementParam.Part.builder().id("1").trader(adminAddress).positionId(positionId1.toString()).positionToken(ERC20_ID.toString()).positionAmount("100").fee("1").timestamp("1653622572").signature("").build();
        SettlementParam.Part orderB = SettlementParam.Part.builder().id("2").trader(accountAAddress).positionId(positionId2.toString()).positionToken(ERC20_ID.toString()).positionAmount("-100").fee("1").timestamp("1653622572").signature("").build();
        SettlementParam.Settlement settlement = SettlementParam.Settlement.builder().stNo("1").positionSold("10").partAFee("1").partBFee("1").build();
        SettlementParam param = SettlementParam.builder().partA(orderA).partB(orderB).settlement(settlement).build();
        zkSync2TxBiz.settlement(param);

        //withdraw position
        zkSync2TxBiz.adminPositionWithdraw(positionId1,ERC20_ID,BigInteger.valueOf(5L));
        zkSync2TxBiz.internalPositionWithdraw(positionId2,ERC20_ID,BigInteger.valueOf(5L),accountAPrivateKey);

        //withdraw:from perpetual balance to token balance
        zkSync2TxBiz.adminWithdraw(ERC20_ID,BigInteger.valueOf(5L));
        zkSync2TxBiz.internalWithdraw(ERC20_ID,BigInteger.valueOf(5L),accountAPrivateKey);
    }
}
