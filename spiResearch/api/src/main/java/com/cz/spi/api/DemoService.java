package com.cz.spi.api;


import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * open api interface
 *
 * @author Zjianru
 */
@SPI
public interface DemoService {

    @Adaptive
    void sayHello(String name);

    String doSomething(String process);
}
