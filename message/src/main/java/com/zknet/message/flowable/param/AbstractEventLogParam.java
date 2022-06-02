package com.zknet.message.flowable.param;

import cn.hutool.json.JSONUtil;
import com.zknet.engine.constants.PlatformEnum;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEventLogParam {

    private String contractAddress;

    private String topic;

    private String txHash;

    private Long blockNo;

    private String data;


    private PlatformEnum platformEnum;

    public String toString() {
        return JSONUtil.parse(this).toString();
    }
}
