package com.cz.spi.service;

import com.cz.spi.api.DemoService;

/**
 * demo logic,Business logic that is loaded or specified by default
 *
 * @author Zjianru
 */
public class AnotherDemoLogic implements DemoService {
    @Override
    public void sayHello(String name) {
        System.out.println("AnotherDemoLogic say hello " + name);
    }

    @Override
    public String doSomething(String process) {
        return "another demo logic out--->" + process;
    }
}
