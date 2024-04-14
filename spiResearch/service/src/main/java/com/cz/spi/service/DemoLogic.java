package com.cz.spi.service;

import com.cz.spi.api.DemoService;

/**
 * demo logic,Business logic that is loaded or specified by default
 *
 * @author Zjianru
 */
public class DemoLogic implements DemoService {
    @Override
    public void sayHello(String name) {
        System.out.println("DemoLogic say hello " + name);
    }

    @Override
    public String doSomething(String process) {
        return "demo logic out--->" + process;
    }
}
