package com.zknet.engine.service;

import com.zknet.engine.constants.PlatformEnum;
import com.zknet.engine.entity.ZknetContractLogEventHandleProgress;

public interface ZknetContractLogEventHandleProgressService {
    Long getLatestBlockNoByTopicAndPlatform(String topic, PlatformEnum platform);
    ZknetContractLogEventHandleProgress getRecordByTopicAndPlatform(String topic, PlatformEnum platform);
    int save(ZknetContractLogEventHandleProgress progress);
    int update(ZknetContractLogEventHandleProgress progress);
}
