package com.cz.bytebuddy.demo;

import com.cz.bytebuddy.demo.pojo.User;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * code desc
 *
 * @author Zjianru
 */
public class DynamicEnhancement {


    public static void main(String[] args) {
        // 尝试动态生成一个User类的子类
        try {
            {
                User testUser = new User().queryUserByName("testUser");
                System.out.println(testUser.toString());
            }

            String path = CreateClass.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String methodName = "queryUserByName";
            String methodParam = "testUserParam";
            DynamicType.Unloaded<User> unloaded = new ByteBuddy()
                    .subclass(User.class)
                    .name("com.cz.gen.genUser")
                    // Interception method by method name
                    .method(getMethodMatcher(methodName))
                    // Interceptor 指定拦截到方法后要执行的处理
                    .intercept(getEnhancedReturnValue(methodName, methodParam, User.class))
                    .make();
            // 加载动态生成的类
            DynamicType.Loaded<User> loaded = unloaded.load(PileTheExampleMethod.class.getClassLoader());

            // 创建该类的一个实例，并调用其toString方法，验证拦截器是否生效
            User testUser = loaded.getLoaded().newInstance().queryUserByName("testUser2222");
            System.out.println(testUser);

            // 将动态生成的类保存到指定路径
            unloaded.saveIn(new File(path));
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            // 如果在动态生成类的过程中发生异常，则抛出运行时异常
            throw new RuntimeException(e);
        }
    }

    private static Implementation getEnhancedReturnValue(String methodName, String methodParam, Class<?> aClass) throws IllegalAccessException {
        Object invoke;
        try {
            methodParam = methodParam + " after byte buddy enhanced";
            Method method = aClass.getMethod(methodName, String.class);

            // 添加监控运行耗时的逻辑
            long startTime = System.nanoTime(); // 记录开始时间
            invoke = method.invoke(aClass.newInstance(), methodParam);
            long endTime = System.nanoTime(); // 记录结束时间
            long duration = endTime - startTime; // 计算耗时
            System.out.printf("方法 %s 耗时: %d 纳秒%n", methodName, duration);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        return FixedValue.value(invoke);

    }


    private static ElementMatcher getMethodMatcher(String methodName) {
        return ElementMatchers.named(methodName)
//                .and(ElementMatchers.returns(String.class))
                // 嵌套条件
//                .or((ElementMatchers.named("sss").or(ElementMatchers.returns(String.class))))
//                // 表达 [ 拦截返回值为对象类型的方法 ]
//                .or(
//                        (
//                                ElementMatchers.returns(Object.class)
//                                        // 表达 [返回值为 void 类型]
//                                .or(ElementMatchers.returns(TypeDescription.ForLoadedType.of(Void.class)))
//                        )
//                )
                ;
    }

}
