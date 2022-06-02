package com.zknet.engine.service.impl;

import com.zknet.engine.service.TokenReceiverAddressService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenReceiverAddressServiceImpl implements TokenReceiverAddressService {
    @Override
    public Set<String> getTokenReceiverAddressSet() {
        //todo reload from db and cache into redis or localCache
        Set<String> receivers = new HashSet<>();
        receivers.add("");
        return receivers;
    }
}
