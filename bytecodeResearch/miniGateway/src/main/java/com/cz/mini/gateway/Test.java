package com.cz.mini.gateway;

import com.cz.mini.gateway.annotation.RpcGatewayClass;
import com.cz.mini.gateway.annotation.RpcGatewayMethod;
import com.cz.mini.gateway.repo.Repo;
import com.cz.mini.gateway.repo.intercept.DemoRepoInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.IOException;


/**
 * code desc
 *
 * @author Zjianru
 */
public class Test {
    public static void main(String[] args) {
        // 尝试动态生成并加载一个类，以演示ByteBuddy库的使用
        try {
            // 获取当前运行代码的路径，用于后续动态类的保存
            String path = Test.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            // 构建动态生成类的全限定名，该类将作为Repo的子类
            String testRepoPath = Repo.class.getPackage().getName().concat(".").concat("TestRepo");
            // 定义目标方法名，该方法将被拦截并增强
            String methodName = "queryData";

            // 使用ByteBuddy动态生成一个类，该类继承自Repo<String>，并拦截名为methodName的方法
            DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                    .subclass(TypeDescription.Generic.Builder.parameterizedType(Repo.class, String.class).build())
                    .name(testRepoPath)
                    .method(ElementMatchers.named(methodName))
                    .intercept(MethodDelegation.to(DemoRepoInterceptor.class))
                    // 为生成的类的方法添加RpcGatewayMethod注解
                    .annotateMethod(AnnotationDescription.Builder.ofType(RpcGatewayMethod.class)
                            .define("name", "methNameAfterEnhanced")
                            .define("desc", "methDescAfterEnhanced").build()
                    )
                    // 为生成的类添加RpcGatewayClass注解
                    .annotateType(AnnotationDescription.Builder.ofType(RpcGatewayClass.class)
                            .define("classDesc", "classDescAfterEnhanced")
                            .define("alias", "aliasAfterEnhanced")
                            .define("timeOut", "timeOutAfterEnhanced").build()
                    ).make();

            // 将动态生成的类保存到指定路径
            unloaded.saveIn(new File(path));
            // 加载刚刚动态生成的类
            Class<?> loaded = Class.forName(testRepoPath);

            // 获取动态生成类上的RpcGatewayClass注解，打印相关信息
            RpcGatewayClass annotationForClass = loaded.getAnnotation(RpcGatewayClass.class);
            System.out.println("print class annotation info ...");
            System.out.println(annotationForClass.classDesc());
            System.out.println(annotationForClass.alias());
            System.out.println(annotationForClass.timeOut());

            // 获取动态生成类的指定方法上的RpcGatewayMethod注解，打印相关信息
            RpcGatewayMethod annotationForMethod = loaded.getMethod(methodName, int.class).getAnnotation(RpcGatewayMethod.class);
            System.out.println("print method annotation info ...");
            System.out.println(annotationForMethod.name());
            System.out.println(annotationForMethod.desc());
        } catch (NoSuchMethodException | IOException | ClassNotFoundException e) {
            // 抛出运行时异常，以处理任何捕获的异常
            throw new RuntimeException(e);
        }

    }
}
