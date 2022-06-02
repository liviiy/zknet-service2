package com.zknet.engine.mapper;

import com.zknet.engine.entity.ZknetContractLogEventHandleProgress;
import org.apache.ibatis.annotations.Param;

public interface ZknetContractLogEventHandleProgressMapper extends BaseMapper<ZknetContractLogEventHandleProgress> {
    Long getLatestBlockNoByTopicAndPlatform(@Param("topic") String topic, @Param("platform") String platform);
}
