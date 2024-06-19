package com.cz.agent;

import com.cz.agent.util.JvmInfoUtils;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * code desc
 * JvmInfoReportAgent 类作为一个代理，用于报告Java虚拟机(JVM)的信息。
 *
 * @author Zjianru
 */
public class JvmInfoReportAgent {

    /**
     * 预加载方法，作为JVM代理的一部分，在主应用的main方法执行前被调用。
     * 此方法启动一个定时任务，每隔5秒打印一次JVM的垃圾回收信息和内存使用情况。
     *
     * @param agentArgs       传递给代理的参数字符串，在此示例中未使用。
     * @param instrumentation JVM提供的Instrumentation实例，允许对类加载器和已加载的类进行高级操作。
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        // 输出当前执行方法的标记信息
        System.out.println("current method ==> JvmInfoReportAgent.premain");

        // 创建一个单线程的定时任务线程池，并安排任务以固定周期执行
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            // 打印JVM的垃圾收集信息
            JvmInfoUtils.printGCInfo();
            // 打印JVM的内存信息
            JvmInfoUtils.printJvmMemInfo();
        }, 0, 5000, TimeUnit.MILLISECONDS); // 初始延迟0毫秒，之后每隔5秒执行一次
    }

}
