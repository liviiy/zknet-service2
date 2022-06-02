package com.zknet.message.flowable.handler;

import com.zknet.engine.entity.ZknetContractLog;
import com.zknet.engine.entity.ZknetContractLogEventHandleProgress;
import com.zknet.engine.service.ZkNetEventService;
import com.zknet.engine.service.ZknetContractLogEventHandleProgressService;
import com.zknet.engine.service.ZknetContractLogService;
import com.zknet.message.flowable.param.AbstractEventLogParam;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public abstract class EventLogHandler {

    @Resource
    protected ZkNetEventService zkNetEventService;

    @Resource
    protected ZknetContractLogService zknetContractLogService;

    @Resource
    protected ZknetContractLogEventHandleProgressService zknetContractLogEventHandleProgressService;

    protected void insertLog(AbstractEventLogParam param) {
        ZknetContractLog zknetContractLog = new ZknetContractLog();
        BeanUtils.copyProperties(param, zknetContractLog);
        zknetContractLog.setPlatform(param.getPlatformEnum().name());
        zknetContractLog.setLogJson(param.toString());
        zknetContractLogService.save(zknetContractLog);
    }

    protected void saveOrUpdateProgress(AbstractEventLogParam param) {
        ZknetContractLogEventHandleProgress progress = zknetContractLogEventHandleProgressService.getRecordByTopicAndPlatform(param.getTopic(), param.getPlatformEnum());
        if (progress == null) {
            progress = new ZknetContractLogEventHandleProgress();
            progress.setPlatform(param.getPlatformEnum().name());
            progress.setTopic(param.getTopic());
            progress.setCompleteBlockNo(param.getBlockNo());
            zknetContractLogEventHandleProgressService.save(progress);
            return;
        }
        progress.setCompleteBlockNo(param.getBlockNo());
        zknetContractLogEventHandleProgressService.update(progress);
    }

    public abstract void handleHash(AbstractEventLogParam param);
}
