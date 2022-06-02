package com.zknet.message.flowable.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.zknet.engine.constants.ContractMethodEnum;
import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.service.Web3Service;
import com.zknet.message.flowable.AbstractEventListener;
import com.zknet.message.flowable.handler.EventLogHandler;
import com.zknet.message.flowable.param.AbstractEventLogParam;
import com.zknet.message.flowable.handler.Zksync2ChainWithdrawEventLogHandler;
import com.zknet.message.flowable.param.WithdrawEventLogParam;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * l2 withdraw subscribe handler
 */
@Slf4j
@Service
public class Zksync2ChainWithdrawEventLogListener extends AbstractEventListener {

    @Autowired
    protected Web3Service defaultWeb3Service;


    @Override
    public PlatformEnum getPlatform() {
        return PlatformEnum.ZKSYNC2;
    }


    @Override
    public List<Event> getEvents() {
        Event event = new Event(ContractMethodEnum.WITHDRAW.getEvent(), Arrays.asList(
                new TypeReference<Uint32>(true) {
                }, new TypeReference<Address>(true) {
                }, new TypeReference<Uint256>(true) {
                }));
        return Arrays.asList(event);
    }

    @Override
    public Class getHander() {
        return Zksync2ChainWithdrawEventLogHandler.class;
    }

    protected Consumer<Log> buildLogConsumer() {
        return ethLog -> {
            log.info("receive {} chain withdraw Log:{}", getPlatform(), ethLog);
            String txHash = ethLog.getTransactionHash();
            // current  contract address
            String contractAddress = ethLog.getAddress();
            BigInteger blockNumber = ethLog.getBlockNumber();
            String data = ethLog.getData();
            List<String> topics = ethLog.getTopics();
            // TODO resolve event
            String topic = topics.get(0);

            BigInteger tokenId = Numeric.toBigInt(topics.get(1));
            String account = new Address(topics.get(2)).toString();
            BigInteger amount = Numeric.toBigInt(topics.get(3));

            if (!EventEncoder.encode(getEvents().get(0)).equals(topic)) {
                log.warn("receive topic:[{}] not equal subscribed topic:[{}],ignore", topic, EventEncoder.encode(getEvents().get(0)));
                return;
            }
            EventLogHandler eventLogHandler = (EventLogHandler) SpringUtil.getBean(getHander());
            AbstractEventLogParam logParam = new WithdrawEventLogParam(contractAddress, topic, txHash, blockNumber.longValue(), data, getPlatform()
                    , tokenId, account, amount);
            eventLogHandler.handleHash(logParam);
        };
    }
}
