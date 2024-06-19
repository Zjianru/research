package com.cz.bytebuddy.demo;

import com.cz.bytebuddy.demo.pojo.User;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.IOException;

/**
 * code desc
 *
 * @author Zjianru
 */
public class PileTheExampleMethod {

    public static void main(String[] args) {
        // 尝试动态生成一个User类的子类
        try {
            // 获取当前运行代码的路径
            String path = CreateClass.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            // 使用ByteBuddy创建一个动态类型，继承自User类，命名为"com.cz.gen.genUser"
            // 并且拦截toString方法，使其返回固定值"hello after bytebuddy intercept"
            DynamicType.Unloaded<User> unloaded = new ByteBuddy()
                    .subclass(User.class)
                    .name("com.cz.gen.genUser")
                    // Interception method by method name
                    .method(ElementMatchers.named("toString"))
                    // Interceptor 指定拦截到方法后要执行的处理
                    .intercept(FixedValue.value("hello after bytebuddy intercept"))
                    .make();

            // 加载动态生成的类
            DynamicType.Loaded<User> loaded = unloaded.load(PileTheExampleMethod.class.getClassLoader());

            // 创建该类的一个实例，并调用其toString方法，验证拦截器是否生效
            String string = loaded.getLoaded().newInstance().toString();
            System.out.println(string);

            // 将动态生成的类保存到指定路径
            unloaded.saveIn(new File(path));
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            // 如果在动态生成类的过程中发生异常，则抛出运行时异常
            throw new RuntimeException(e);
        }

    }


}
