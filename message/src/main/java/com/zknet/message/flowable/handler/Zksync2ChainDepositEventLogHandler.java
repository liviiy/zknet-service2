package com.zknet.message.flowable.handler;

import com.zknet.engine.constants.TransactionFlowTypeEnum;
import com.zknet.engine.constants.TxStatusEnum;
import com.zknet.engine.entity.ZkNetEvent;
import com.zknet.message.flowable.param.AbstractEventLogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service("zksync2ChainDepositEventLogHandler")
public class Zksync2ChainDepositEventLogHandler extends EventLogHandler {

    @Override
    public void handleHash(AbstractEventLogParam param) {
        ZkNetEvent zkNetEvent = zkNetEventService.findByTxHash(param.getTxHash());
        //todo need more action ?
        if (zkNetEvent == null) {
            log.warn("Deposit Event has no Tx record, log:{}", param);
            return;
        }
        TransactionFlowTypeEnum transactionFlowTypeEnum = zkNetEvent.getType();
        if (transactionFlowTypeEnum == null) {
            log.warn("ZkNetEvent record has invalid type,zkNetEvent:{}", zkNetEvent);
            return;
        }
        if (transactionFlowTypeEnum.isFinalStatus(zkNetEvent.getTxStatus())) {
            log.warn("ZkNetEvent record tx status already final:{}", zkNetEvent);
            return;
        }
        zkNetEvent.setTxStatus(TxStatusEnum.L2_SUCCESS);
        zkNetEventService.update(zkNetEvent);
        insertLog(param);
        saveOrUpdateProgress(param);
    }
}
