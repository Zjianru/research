package com.cz.spi.api;


import org.apache.dubbo.common.extension.SPI;

/**
 * open api interface
 *
 * @author Zjianru
 */
@SPI
public interface DemoService {

    void sayHello(String name);

    String doSomething(String process);
}
