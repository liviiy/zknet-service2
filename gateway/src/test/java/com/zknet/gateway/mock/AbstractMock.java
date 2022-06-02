package com.zknet.gateway.mock;

import cn.hutool.json.JSONUtil;
import com.zknet.gateway.config.filter.CustomFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
public abstract class AbstractMock {


    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CustomFilter())
                .build();
    }

    @SneakyThrows
    public void request(Map<String, Object> map) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", "333");
        httpHeaders.add("Content-Type","application/json-rpc");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(UriComponentsBuilder.fromUriString("http://121.89.125.161/zknet").buildAndExpand().encode().toUri())
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/zknet")
                        .headers(httpHeaders)
                        .content(JSONUtil.parse(map).toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value("101002"))
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

}
