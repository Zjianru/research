package com.cz.spi.out;

import com.cz.spi.api.DemoService;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.ServiceLoader;
import java.util.Set;

/**
 * code desc
 *
 * @author Zjianru
 */
public class CheckLogic {
    public static void main(String[] args) {
        spiByJdk();
        spiByDubbo();
    }



    private static void spiByDubbo() {
        // 获取扩展加载器
        ExtensionLoader<DemoService> loader = ExtensionLoader.getExtensionLoader(DemoService.class);
        // 获取所有支持的扩展
        Set<String> extensions = loader.getSupportedExtensions();
        System.out.println("------------dobbo spi start load service --------------");
        for (String extension : extensions) {
            DemoService service = loader.getExtension(extension);
            outServiceInfo(service);
        }
        System.out.println("------------dobbo spi end load service --------------");
    }

    private static void spiByJdk() {
        ServiceLoader<DemoService> services = ServiceLoader.load(DemoService.class);
        System.out.println("------------jdk spi start load service --------------");
        for (DemoService service : services) {
            outServiceInfo(service);
        }
        System.out.println("------------jdk spi end load service --------------");
    }

    private static void outServiceInfo(DemoService service) {
        Class<? extends DemoService> serviceClass = service.getClass();
        String currentServiceName = serviceClass.getName();
        System.out.println("current load service class -->" + serviceClass);
        System.out.println("current load service is -->" + currentServiceName);
        System.out.println("------------ start call function --------------");
        service.sayHello(currentServiceName);
        String processed = service.doSomething(currentServiceName);
        System.out.println("service " + currentServiceName + " processed result is -->" + processed);
        System.out.println("------------ end call function --------------");
    }
}
