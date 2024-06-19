package com.cz.bytebuddy.demo;

import com.cz.bytebuddy.demo.pojo.User;
import com.cz.bytebuddy.demo.util.TimeConsuming;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
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
        String path = CreateClass.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        // 拦截已有方法并完成增强
//        interceptMethod(path);
        // 生成新方法Modifier
//        createNewMethod(path);
        // 生成新属性
//        createNewField(path);
        // 方法委托
        methodDelegation(path);

    }


    /**
     * 通过ByteBuddy实现方法委托，将User类中queryUserByName方法的调用委托给TimeConsuming类处理。
     * 这样做的目的是为了演示如何使用ByteBuddy动态地修改类的行为，而不改变原有类的代码。
     *
     * @param path 保存修改后的类文件的路径。
     */
    private static void methodDelegation(String path) {

        try {
            // 创建一个动态类型，继承自User类，并重命名为GenUser。
            // 方法queryUserByName的调用被委托给TimeConsuming类处理。
            DynamicType.Unloaded<User> unloaded = new ByteBuddy().subclass(User.class).name("com.cz.bytebuddy.demo.pojo.GenUser").method(ElementMatchers.named("queryUserByName")).intercept(MethodDelegation.to(TimeConsuming.class)).make();

            // 加载生成的类，并实例化。
            Class<? extends User> loaded = unloaded.load(User.class.getClassLoader()).getLoaded();
            Object invoke = loaded.getMethod("queryUserByName", String.class).invoke(loaded.newInstance(), "cz");
            System.out.println("invoke result==>" + invoke.toString());

        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过ByteBuddy为User类动态添加两个字段enhanceField1和enhancedField2。
     * 这样做的目的是为了展示如何使用ByteBuddy动态地修改类的结构，增加新的属性。
     *
     * @param path 保存修改后的类文件的路径。
     */
    private static void createNewField(String path) {
        try {
            // 创建一个动态类型，重新定义User类，并重命名为GenUser。
            // 添加两个属性enhanceField1和enhancedField2。
            DynamicType.Unloaded<User> unloaded = new ByteBuddy().redefine(User.class).name("com.cz.bytebuddy.demo.pojo.GenUser").defineProperty("enhanceField1", String.class).defineProperty("enhancedField2", User.class).make();

            // 将生成的类文件保存到指定路径。
            unloaded.saveIn(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过ByteBuddy为User类动态添加一个新方法enhanceMethodByByteBuddy。
     * 这样做的目的是为了展示如何使用ByteBuddy动态地为类增加新的行为。
     *
     * @param path 保存修改后的类文件的路径。
     */
    private static void createNewMethod(String path) {
        try {
            // 创建一个动态类型，重新定义User类，并重命名为GenUser。
            // 定义一个新方法enhanceMethodByByteBuddy，并固定其返回值为"enhanceMethodByByteBuddy"。
            DynamicType.Unloaded<User> unloaded = new ByteBuddy().redefine(User.class).name("com.cz.bytebuddy.demo.pojo.GenUser").defineMethod("enhanceMethodByByteBuddy", String.class).withParameter(String.class, "params").intercept(FixedValue.value("enhanceMethodByByteBuddy")).make();

            // 将生成的类文件保存到指定路径。
            unloaded.saveIn(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过ByteBuddy动态拦截方法，修改返回值。
     * 该方法演示了如何动态地增强一个类的方法，具体来说，它拦截了User类的queryUserByName方法，
     * 并在方法执行后修改了返回值。
     *
     * @param path 保存动态生成类的文件路径。
     */
    private static void interceptMethod(String path) {
        try {
            // 定义要拦截的方法名和参数
            String methodName = "queryUserByName";
            String methodParam = "testUserParam";

            // 使用ByteBuddy创建一个User类的子类，命名为com.cz.gen.genUser
            DynamicType.Unloaded<User> unloaded = new ByteBuddy().subclass(User.class).name("com.cz.gen.genUser")
                    // 使用getMethodMatcher(methodName)定义匹配方法的规则
                    // Interception method by method name
                    .method(getMethodMatcher(methodName))
                    // 拦截匹配到的方法，并应用getEnhancedReturnValue(methodName, methodParam, User.class)中定义的逻辑
                    // Interceptor 指定拦截到方法后要执行的处理
                    .intercept(getEnhancedReturnValue(methodName, methodParam, User.class)).make();

            // 加载动态生成的类
            // 加载动态生成的类
            DynamicType.Loaded<User> loaded = unloaded.load(PileTheExampleMethod.class.getClassLoader());
            // 创建动态类的实例，并调用被拦截的方法
            // 创建该类的一个实例，并调用其toString方法，验证拦截器是否生效
            User testUser = loaded.getLoaded().newInstance().queryUserByName("testUser2222");
            System.out.println(testUser);
            // 将动态生成的类保存到指定路径
            // 将动态生成的类保存到指定路径
            unloaded.saveIn(new File(path));
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            // 如果在动态生成类的过程中发生异常，则抛出运行时异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取增强返回值的实现。
     * 该方法通过ByteBuddy的拦截机制，动态修改方法的返回值。它首先调用原方法获取返回值，
     * 然后对返回值进行修改，最后返回一个FixedValue.value(invoke)的实现，用于固定修改后的返回值。
     *
     * @param methodName  被拦截方法的名称。
     * @param methodParam 被拦截方法的参数。
     * @param aClass      被拦截方法所在的类。
     * @return 修改后的返回值的实现。
     * @throws IllegalAccessException 如果访问字段或方法时发生非法访问。
     */
    private static Implementation getEnhancedReturnValue(String methodName, String methodParam, Class<?> aClass) throws IllegalAccessException {
        Object invoke;
        try {
            // 修改方法参数
            methodParam = methodParam + " after byte buddy enhanced";
            // 获取方法并调用
            Method method = aClass.getMethod(methodName, String.class);
            invoke = method.invoke(aClass.newInstance(), methodParam);
            // 记录方法执行时间，用于性能调试
            // 添加监控运行耗时的逻辑
            long startTime = System.nanoTime(); // 记录开始时间
            invoke = method.invoke(aClass.newInstance(), methodParam);
            long endTime = System.nanoTime(); // 记录结束时间
            long duration = endTime - startTime; // 计算耗时
            System.out.printf("方法 %s 耗时: %d 纳秒%n", methodName, duration);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        // 返回修改后的返回值
        return FixedValue.value(invoke);
    }

    /**
     * 获取方法匹配器。
     * 该方法用于定义ByteBuddy如何匹配要拦截的方法。这里匹配方法名为methodName的方法。
     *
     * @param methodName 要匹配的方法名。
     * @return 方法匹配器。
     */
    private static ElementMatcher getMethodMatcher(String methodName) {
        // 通过ElementMatchers.named(methodName)匹配方法名
        return ElementMatchers.named(methodName);
    }
}
