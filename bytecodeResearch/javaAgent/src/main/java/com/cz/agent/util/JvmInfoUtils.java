package com.cz.agent.util;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.List;

/**
 * 工具类，用于获取并打印JVM的内存信息和垃圾回收信息。
 *
 * @author Zjianru
 */
public class JvmInfoUtils {

    // 定义1MB的字节大小，用于后续内存使用量的转换和显示
    private static final long MB = 1048576L;

    /**
     * 打印JVM的内存使用信息，包括堆内存和非堆内存的初始化大小、最大大小、已使用大小、已提交大小以及使用率。
     */
    public static void printJvmMemInfo() {
        // 获取内存管理器的实例
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        // 获取堆内存的使用情况
        MemoryUsage usage = memory.getHeapMemoryUsage();
        // 格式化并打印堆内存的使用信息
        String info = String.format("\ninit: %s\t max: %s\t used: %s\t committed: %s\t use rate: %s\n ", usage.getInit() / MB + "MB", usage.getMax() / MB + "MB", usage.getUsed() / MB + "MB", usage.getCommitted() / MB + "MB", usage.getUsed() * 100 / usage.getCommitted() + "%");
        System.out.print(info);
        // 获取非堆内存的使用情况
        MemoryUsage nonHeadMemory = memory.getNonHeapMemoryUsage();
        // 格式化并打印非堆内存的使用信息
        info = String.format("init: %s\t " + "max: %s\t" + " used: %s\t " + "committed: % s\t " + "use rate: %s\n ", nonHeadMemory.getInit() / MB + "MB", nonHeadMemory.getMax() / MB + "MB", nonHeadMemory.getUsed() / MB + "MB", nonHeadMemory.getCommitted() / MB + "MB", nonHeadMemory.getUsed() * 100 / nonHeadMemory.getCommitted() + "%");
        System.out.println(info);
    }

    /**
     * 打印JVM的垃圾回收器信息，包括垃圾回收器名称、回收次数、回收总耗时以及关联的内存池名称。
     */
    public static void printGCInfo() {
        // 获取所有垃圾回收器的实例
        List<GarbageCollectorMXBean> garbage = ManagementFactory.getGarbageCollectorMXBeans();
        // 遍历每个垃圾回收器，打印其相关信息
        for (GarbageCollectorMXBean item : garbage) {
            // 格式化并打印垃圾回收器的信息
            String info = String.format("name: %s\t count:%s\t took:%s\t pool name:%s ", item.getName(), item.getCollectionCount(), item.getCollectionTime(), Arrays.deepToString(item.getMemoryPoolNames()));
            System.out.println(info);
        }
    }
}

