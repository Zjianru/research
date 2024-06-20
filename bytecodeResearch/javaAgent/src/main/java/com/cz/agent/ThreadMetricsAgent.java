package com.cz.agent;

import com.cz.agent.advice.ThreadMetricsAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * code desc
 *
 * @author Zjianru
 */
public class ThreadMetricsAgent {
    /**
     * JavaAgent的入口方法。在JVM启动时调用。
     * 用于对指定包名下的类进行字节码增强，以实现方法执行时间的监控。
     *
     * @param agentArgs agent的参数，本例中未使用。
     * @param inst      Java虚拟机的Instrumentation实例，用于实现字节码增强。
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("基于javaagent链路追踪");

        // 创建一个转换器，用于对匹配的类进行字节码增强
        // 定义一个转换器，用于拦截符合名称条件的所有类的方法，并将执行委托给MethodCostTime类。
        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, module, protectionDomain) -> {
            // 对所有方法进行增强，但排除名称以"main"开头的方法
            builder.visit(
                    Advice.to(ThreadMetricsAdvice.class)
                            .on(
                                    ElementMatchers.isMethod()
                                            .and(ElementMatchers.any())
                                            .and(ElementMatchers.not(ElementMatchers.nameStartsWith("main")))
                            )
            );
            return builder;
        };

        // 创建一个监听器，用于监听类转换的过程
        // 定义一个监听器，用来监视类转换过程中的各个阶段。
        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            // 当发现需要转换的类时调用
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                // 发现匹配类时调用，可用于记录日志或其他操作。
            }

            // 当类转换成功时调用
            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                // 类转换成功后调用，可记录转换结果。
            }

            // 当有类被忽略未转换时调用
            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
                // 类被忽略未转换时调用，可用于记录信息。
            }

            // 当类转换发生错误时调用
            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                // 转换类时发生错误时调用，可用于错误处理及记录。
            }

            // 当所有类转换完成时调用
            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                // 类转换完成时调用，可用于记录或进行其他操作。
            }
        };

        // 使用AgentBuilder对包名为"com.cz"开头的类进行字节码增强
        // 这里指定了监听器和转换器，并将增强应用到Instrumentation中
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("com.cz"))
                .transform(transformer)
                .with(listener)
                .installOn(inst);
    }


    //如果代理类没有实现上面的方法，那么 JVM 将尝试调用该方法
    public static void premain(String agentArgs) {
    }


}
