package com.zknet.gateway.jsonrpc.api.impl;

import cn.hutool.json.JSONUtil;
import com.zknet.gateway.biz.TxBiz;
import com.zknet.gateway.jsonrpc.param.DepositParam;
import com.zknet.gateway.jsonrpc.param.WithdrawParam;
import com.zknet.gateway.mock.AbstractMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkNetServerApiImplTest extends AbstractMock {


    @Test
    public void deposit() {
        DepositParam depositParam = new DepositParam();
        depositParam.setAddress("0x644b8D3927AFCa6646D0e448DC45C4fBfE04a82e");
        depositParam.setTimestamp(1650593029);
        depositParam.setToken(BigInteger.valueOf(10));
        depositParam.setAmount("0.01");
        depositParam.setSign("0x001e082798732dd5ac4c054b5e1e8bcfc584bf48d1518452e843c36f6ac1466a4b72dcdd471596d1e914f44a69570c23bb6340df4b88d3ef7be242aeb97831b8cd1b");

        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("jsonrpc", "2.0");
        map.put("method", "zkn_deposit");
        map.put("params", Arrays.asList(JSONUtil.parse(depositParam)));
        this.request(map);
    }

    @Test
    public void withdraw() {
        WithdrawParam depositParam = new WithdrawParam();
        depositParam.setAddress("");
        depositParam.setTimestamp(null);
        depositParam.setToken(BigInteger.ONE);
        depositParam.setAmount("");
        depositParam.setSign("");

        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("jsonrpc", "2.0");
        map.put("method", "zkn_withdraw");
        map.put("params", Arrays.asList(JSONUtil.parse(depositParam)));
        this.request(map);
    }
}
