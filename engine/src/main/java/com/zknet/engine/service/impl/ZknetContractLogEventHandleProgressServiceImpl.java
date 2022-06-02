package com.zknet.engine.service.impl;

import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.entity.ZknetContractLogEventHandleProgress;
import com.zknet.engine.mapper.ZknetContractLogEventHandleProgressMapper;
import com.zknet.engine.service.ZknetContractLogEventHandleProgressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZknetContractLogEventHandleProgressServiceImpl implements ZknetContractLogEventHandleProgressService {

    @Resource
    private ZknetContractLogEventHandleProgressMapper zknetContractLogEventHandleProgressMapper;
    @Override
    public Long getLatestBlockNoByTopicAndPlatform(String topic, PlatformEnum platform) {
        return zknetContractLogEventHandleProgressMapper.getLatestBlockNoByTopicAndPlatform(topic,platform.name());
    }

    @Override
    public ZknetContractLogEventHandleProgress getRecordByTopicAndPlatform(String topic, PlatformEnum platform) {
        ZknetContractLogEventHandleProgress progress = new ZknetContractLogEventHandleProgress();
        progress.setTopic(topic);
        progress.setPlatform(platform.name());
        return zknetContractLogEventHandleProgressMapper.selectOne(progress);
    }

    @Override
    public int save(ZknetContractLogEventHandleProgress progress) {
        return zknetContractLogEventHandleProgressMapper.insertSelective(progress);
    }

    @Override
    public int update(ZknetContractLogEventHandleProgress progress) {
        return zknetContractLogEventHandleProgressMapper.updateByPrimaryKeySelective(progress);
    }
}
