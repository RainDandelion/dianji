package com.netty.Server.impl;

import com.netty.Server.IBusinessServer;
import org.springframework.stereotype.Service;

@Service
public class BusinessServerImpl implements IBusinessServer {
    @Override
    public void testBus() {
        System.out.println("测试线程池 业务逻辑");
    }
}
