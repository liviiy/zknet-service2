package com.zknet.message.flowable;

import cn.hutool.core.collection.CollectionUtil;
import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.entity.ZknetBusinessContract;
import com.zknet.engine.service.TokenReceiverAddressService;
import com.zknet.engine.service.Web3Service;
import com.zknet.engine.service.ZknetBusinessContractService;
import com.zknet.engine.service.ZknetContractLogEventHandleProgressService;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventListener  {

    @Autowired
    protected Web3Service defaultWeb3Service;

    @Resource
    protected ZknetBusinessContractService zknetBusinessContractService;

    @Resource
    protected ZknetContractLogEventHandleProgressService zknetContractLogEventHandleProgressService;

    @Resource
    protected TokenReceiverAddressService tokenReceiverAddressService;


    public List<String> getContractAddressByPlatform(PlatformEnum platformEnum) {
        List<ZknetBusinessContract> list = zknetBusinessContractService.getListByPlatform(platformEnum);
        return list.stream().map(e -> e.getContractAddress()).distinct().collect(Collectors.toList());
    }


    public DefaultBlockParameter getLatestBlockNoByTopicAndPlatform(String topic, PlatformEnum platform) {
        Long maxBlockNo = zknetContractLogEventHandleProgressService.getLatestBlockNoByTopicAndPlatform(topic,platform);
        if (maxBlockNo == null || maxBlockNo == 0) {
//            return DefaultBlockParameterName.EARLIEST;
            // todo it seems Block range should not be less than or equal to 100
            return DefaultBlockParameter.valueOf(BigInteger.valueOf(100));
        }
        return DefaultBlockParameter.valueOf(BigInteger.valueOf(maxBlockNo).add(BigInteger.ONE));
    }

    /**
     * init subscribe
     */

    public void init() {
        EthFilter filter = getEthFilter();
        if (filter != null) {
            defaultWeb3Service.subscribeLogEventFlowable(filter, buildLogConsumer(), getPlatform());
        }
    }

    protected EthFilter getEthFilter() {
        List<String> contractList = getContractAddressByPlatform(getPlatform());
        if (CollectionUtil.isEmpty(contractList)) {
            log.warn("no {} chain contract to subscribe", getPlatform());
            return null;
        }
        Event event = getEvents().get(0);
        EthFilter filter = new EthFilter(getLatestBlockNoByTopicAndPlatform(EventEncoder.encode(event), getPlatform()), DefaultBlockParameterName.LATEST, contractList);
        filter.addSingleTopic(EventEncoder.encode(event));
        log.info("subscribing {} topic:{}", event.getName(),EventEncoder.encode(event));
        return filter;
    }

    /**
     * web3 network selection
     *
     * @return
     */
    protected abstract PlatformEnum getPlatform();

    protected abstract List<Event> getEvents();

    protected abstract Class getHander();;

    protected abstract Consumer<Log> buildLogConsumer() ;

}
