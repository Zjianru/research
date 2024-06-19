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
        apiTest.simulatedBusinessMethods();
    }


    public void simulatedBusinessMethods(){
        long start = System.currentTimeMillis();
        System.out.println("come into business method ");
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("business method end, cost time: " + (System.currentTimeMillis() - start));
    }
}
