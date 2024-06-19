package com.cz.mini.gateway.repo.intercept;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * code desc
 *
 * @author Zjianru
 */
public class DemoRepoInterceptor {
    public static String intercept(@Origin Method method, @AllArguments Object[] args) {
        System.out.println("current method is " + method + "and params is ==>" + Arrays.toString(args));
        return "intercepted";
    }

}
