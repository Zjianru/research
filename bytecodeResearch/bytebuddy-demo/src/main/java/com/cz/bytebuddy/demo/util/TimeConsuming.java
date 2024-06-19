package com.cz.bytebuddy.demo.util;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

/**
 *
 *
 * @author Zjianru
 */
public class TimeConsuming {


    /**
     * 装饰方法，用于测量方法的执行时间。
     * 通过使用@SuperCall注解，该方法可以在被装饰的方法执行前后插入额外的操作。
     * 具体来说，它在方法执行前记录当前时间，在方法执行后计算并打印方法的执行时间。
     *
     * @param callable 一个Callable对象，代表被装饰的方法。通过调用callable.call()来执行被装饰的方法。
     * @return 返回被装饰方法的执行结果。
     * @throws Exception 如果被装饰方法抛出异常，该异常将被传递。
     */
    @RuntimeType
    public static Object timeMethod(@SuperCall Callable<?> callable) throws Exception {
        // 记录方法开始执行的时间
        long start = System.currentTimeMillis();
        try {
            // 调用被装饰的方法，并返回结果
            return callable.call();
        } finally {
            // 记录方法执行结束的时间
            long end = System.currentTimeMillis();
            // 计算方法的执行时间
            long elapsedTime = (end - start);
            // 打印方法执行时间
            System.out.printf("Method execution time: %d ms%n", elapsedTime);
        }
    }

}
