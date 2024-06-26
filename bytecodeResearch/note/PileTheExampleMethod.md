# bytebuddy 02 - 对实例方法进行插桩

**插桩** 一般指的是字节码插桩，即对字节码进行增强或修改

**埋点**  插桩是实现埋点的一种方式

对方法进行拦截

```java
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
            loaded.getLoaded().newInstance().toString();

            // 将动态生成的类保存到指定路径
            unloaded.saveIn(new File(path));
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            // 如果在动态生成类的过程中发生异常，则抛出运行时异常
            throw new RuntimeException(e);
        }

```
拦截器可参考 `net.bytebuddy.matcher.ElementMatchers`
