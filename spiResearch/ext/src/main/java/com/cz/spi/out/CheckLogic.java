package com.cz.spi.out;

import com.cz.spi.api.DemoService;

import java.util.ServiceLoader;

/**
 * code desc
 *
 * @author Zjianru
 */
public class CheckLogic {
    public static void main(String[] args) {
        ServiceLoader<DemoService> services = ServiceLoader.load(DemoService.class);
        for (DemoService service : services) {
            Class<? extends DemoService> serviceClass = service.getClass();
            String currentServiceName = serviceClass.getName();
            System.out.println("current load service class -->" + serviceClass);
            System.out.println("current load service is -->" + currentServiceName);
            System.out.println("------------ start call function --------------");
            service.sayHello(currentServiceName);
            service.doSomething(currentServiceName);
        }
    }
}
