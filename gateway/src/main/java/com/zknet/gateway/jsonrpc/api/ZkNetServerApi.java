package com.zknet.gateway.jsonrpc.api;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zknet.engine.constants.TxStatusEnum;
import com.zknet.gateway.jsonrpc.dto.ZkNetL2TokenDTO;
import com.zknet.gateway.jsonrpc.param.*;

import javax.validation.Valid;

@JsonRpcService("zknet")
public interface ZkNetServerApi {
    //    @JsonRpcMethod("zkn_getTokenById")
    ZkNetL2TokenDTO zkn_getTokenById(Long id);

    TxStatusEnum zkn_txStatusByHash(String hash);

    String zkn_getApiKey(ApiKeyQueryParam param);

    @JsonRpcMethod("zkn_bindAccount")
    String bindAccount(@Valid BindAccountParam param);

    @JsonRpcMethod("zkn_deposit")
    String deposit(@Valid DepositParam param);

    @JsonRpcMethod("zkn_withdraw")
    String withdraw(@Valid WithdrawParam param);

    @JsonRpcMethod("zkn_settlement")
    String settlement(SettlementParam param);
}
