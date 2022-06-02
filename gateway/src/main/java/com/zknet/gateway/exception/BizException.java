package com.zknet.gateway.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private BizCodeEnum bizCode;

    private String replaceMsg;

    public BizException(BizCodeEnum bizCode) {
        super(bizCode.getMsg());
        this.bizCode = bizCode;
        this.replaceMsg = bizCode.getMsg();
    }

    public BizException(BizCodeEnum bizCode, String replaceMsg) {
        this(bizCode);
        this.replaceMsg = replaceMsg;
    }
}
