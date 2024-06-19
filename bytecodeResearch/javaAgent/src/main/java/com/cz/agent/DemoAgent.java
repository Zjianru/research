package com.cz.agent;

import com.cz.agent.target.MethodCostTime;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * DemoAgent 类作为一个代理，用于在运行时拦截并修改特定类的行为
 * 实现面向切面编程(AOP)的方式进行监控或增强
 *
 * @author Zjianru
 */
public class DemoAgent {

    /**
     * 代理的入口方法，在应用程序的主方法之前被调用。
     * 它用于动态字节码操作，以实现对特定类的监控或增强。
     *
     * @param agentArgs       启动时传递给代理的参数，用于配置代理行为。
     * @param instrumentation JVM提供的Instrumentation实例，用于类加载和转换操作。
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        // 打印代理进入日志，便于调试。
        System.out.println("进入代理premain阶段，代理参数为 -->" + agentArgs);

        // 定义一个转换器，用于拦截符合名称条件的所有类的方法，并将执行委托给MethodCostTime类。
        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, module, protectionDomain)
                -> builder
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(MethodCostTime.class));

        // 定义一个监听器，用来监视类转换过程中的各个阶段。
        AgentBuilder.Listener listener = new AgentBuilder.Listener() {

            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                // 发现匹配类时调用，可用于记录日志或其他操作。
            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                // 类转换成功后调用，可记录转换结果。
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
                // 类被忽略未转换时调用，可用于记录信息。
            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                // 转换类时发生错误时调用，可用于错误处理及记录。
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                // 类转换完成时调用，可用于记录或进行其他操作。
            }
        };

        // 构建并安装代理，指定转换所有以 "com.cz" 开头的类。
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith("com.cz"))
                .transform(transformer)
                .with(listener)
                .installOn(instrumentation);
    }

    /**
     * 另一个代理入口方法，与premain同名但缺少Instrumentation参数。
     * 在当前上下文中显得冗余或未完成。
     *
     * @param agentArgs 启动时传递给代理的参数。
     */
    public static void premain(String agentArgs) {
        // 此方法当前为空，可能需要进一步实现或移除。
    }

}