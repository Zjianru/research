package com.cz.agent;

import java.util.Random;

/**
 * code desc
 *
 * @author Zjianru
 */
public class ApiTest {

    public static void main(String[] args) {
        ApiTest apiTest = new ApiTest();
        // 模拟业务方法
        apiTest.simulatedBusinessMethods();
        // 模拟线程局部变量
        apiTest.threadLocalMetricsTest();

    }


    public void simulatedBusinessMethods() {
        long start = System.currentTimeMillis();
        System.out.println("come into business method ");
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("business method end, cost time: " + (System.currentTimeMillis() - start));
    }


    public void threadLocalMetricsTest() {
        // 线程一
        new Thread(() -> new ApiTest().http_lt1()).start();
        // 线程二
        new Thread(() -> new ApiTest().http_lt1()).start();
    }

    private void http_lt1() {
        System.out.println("测试结果:hi1");
        http_lt2();
    }

    private void http_lt2() {
        System.out.println("测试结果:hi2");
        http_lt3();
    }

    private void http_lt3() {
        System.out.println("测试结果:hi3");
    }

}
