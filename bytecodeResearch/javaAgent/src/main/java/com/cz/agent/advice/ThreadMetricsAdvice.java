package com.cz.agent.advice;

import com.cz.agent.threadAgent.TrackContext;
import com.cz.agent.threadAgent.TrackManager;
import net.bytebuddy.asm.Advice;

import java.util.UUID;

/**
 * ThreadMetricsAdvice 类用于在方法进入和退出时记录链路追踪信息。
 * 它利用 AspectJ 的切面编程功能，在指定的方法执行前后插入追踪逻辑。
 *
 * @author Zjianru
 */
public class ThreadMetricsAdvice {

    /**
     * 在方法进入时执行。
     * 该方法用于生成并记录当前线程的链路追踪ID，确保每个请求的可追踪性。
     * 如果当前线程尚未绑定链路追踪ID，则生成一个新的UUID并绑定到线程上下文中。
     *
     * @param className  方法所在类的名称。
     * @param methodName 被执行的方法的名称。
     */
    @Advice.OnMethodEnter()
    public static void enter(@Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName) {
        // 获取当前线程的链路追踪ID
        String linkId = TrackManager.getCurrentSpan();
        // 如果没有链路追踪ID，则生成一个新的并设置到线程上下文中
        if (null == linkId) {
            linkId = UUID.randomUUID().toString();
            TrackContext.setLinkId(linkId);
        }
        // 创建一个入口Span，用于记录方法的开始时间等信息
        String entrySpan = TrackManager.createEntrySpan();

        // 打印链路追踪信息，用于调试和监控
        System.out.println("链路追踪:" + entrySpan + " " + className + "." + methodName);
    }

    /**
     * 在方法退出时执行。
     * 该方法主要用于清理链路追踪的出口Span，确保线程上下文的干净。
     * 目前该方法实现中并未对获取的出口Span做任何处理，可根据实际需求进行扩展。
     *
     * @param className  方法所在类的名称。
     * @param methodName 被执行的方法的名称。
     */
    @Advice.OnMethodExit()
    public static void exit(@Advice.Origin("#t") String className,
                            @Advice.Origin("#m") String methodName) {
        // 获取当前线程的出口Span，但并未做进一步处理
        TrackManager.getExitSpan();
    }
}
