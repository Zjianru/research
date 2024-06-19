# bytebuddy 03 - 动态增强

可对返回值、参数进行处理
在处理返回值前后，可指定运行代码，达到拦截的目的

拦截方法的规则可自定义，写法可实现嵌套

## 方式
ByteBuddy 提供了几种不同的方式来创建或修改类，以适应不同的使用场景

1. subclass
   用途: 创建一个目标类的子类。
   特点: 主要用于添加新的方法或覆盖现有方法，同时保留父类的所有行为。适用于需要扩展类功能而不直接修改原始类的情况。
   示例:

```java
   DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
        .subclass(MyOriginalClass.class)
        .method(ElementMatchers.named("someMethod"))
        .intercept(FixedValue.value("Modified Behavior"))
        .make();
```

2. redefine
   用途: 修改已经存在的类的定义。
   特点: 直接在类的字节码层面进行修改，可以用来改变方法的行为、添加或删除字段等。需要注意的是，这通常需要特定的类加载器设置，因为在JVM中类定义通常是不可变的，除非使用特殊的类加载机制。
   示例:

```java
   DynamicType.Unloaded<?> redefinedType = new ByteBuddy()
        .redefine(MyLoadedClass.class)
        .method(ElementMatchers.named("existingMethod"))
        .intercept(MethodDelegation.to(MyInterceptor.class))
        .make();
```

3. rebase
   用途: 扩展一个类，但不直接创建子类。与subclass相似，但允许更灵活地控制类的层次结构。
   特点: 可以看作是介于创建子类和修改现有类之间的一种方式，适合于想要在不改变类继承关系的情况下添加功能的场景。
   示例:

```java
   DynamicType.Unloaded<?> rebasedType = new ByteBuddy()
        .rebase(MyBaseClass.class)
        .method(ElementMatchers.named("anotherMethod"))
        .intercept(FallbackMethod.invokeDefault())
        .make();
```

4. 代理 (Proxy)
   用途: 创建Java代理对象，实现接口的动态代理。
   特点: 不直接修改或创建类，而是通过Java的代理机制在运行时动态地处理方法调用。适合于基于接口的切面编程。
   示例:

```java
   Class<? extends MyInterface> proxyType = new ByteBuddy()
        .subclass(MyInterface.class)
        .method(ElementMatchers.any())
        .intercept(MethodDelegation.to(MyInvocationHandler.class))
        .make()
        .load(getClass().getClassLoader())
        .getLoaded();
MyInterface proxyInstance = proxyType.getDeclaredConstructor().newInstance();
```

5. 组合 (Composite)
   用途: 将多个类的修改组合在一起，形成一个新的类定义。
   特点: 提供了一种灵活的方式来组合不同方面的修改，适合于需要应用多种增强的场景。
   这些方法可以根据具体需求灵活组合使用，以实现复杂的类结构和行为修改。ByteBuddy的设计理念是高度灵活和可扩展，因此开发者可以根据实际应用场景选择最合适的增强方式。

## 方法委托场景下的关键注解

1. `net.bytebuddy.implementation.bind.annotation.Origin`

@Origin 注解用于访问被拦截方法的元数据信息，比如方法签名、所属类等

当在拦截器方法中需要根据原始方法的信息做出决策时，@Origin 就显得尤为重要

这个注解可以绑定到方法参数上，能够访问到被代理方法的相关信息。

例如，在日志记录场景中，你可能需要知道被调用的具体方法名：

```Java
public class LoggingInterceptor {
    public static void logMethodCall(@Origin Method method) {
        System.out.println("Called method: " + method.getName());
    }
}
```

在这里，@Origin Method method 参数允许拦截器方法访问到被拦截方法的 Method 对象，进而获取方法名称等信息。

2. `net.bytebuddy.implementation.bind.annotation.SuperCall`

`@SuperCall` 注解用于代表对被代理方法的调用，即调用原始未修改的行为

当需要在方法调用前后插入一些逻辑（比如日志记录、性能监控），但最终仍需要执行原有方法时，@SuperCall 就变得非常有用

**通常与 `Callable` 结合使用，因为 `Callable` 表示一个可调用的任务，其结果可以等待**

例如，记录方法执行时间的同时调用原方法：

```Java
public class TimingInterceptor {
    public static Object timeAndInvoke(@SuperCall Callable<?> callable) throws Exception {
        long start = System.nanoTime();
        try {
            return callable.call(); // 调用原始方法
        } finally {
            long end = System.nanoTime();
            System.out.println("Execution time: " + (end - start) + " ns");
        }
    }
}
```

在上述代码中，`@SuperCall Callable<?> callable` 参数代表了对被代理方法的实际调用

通过调用 `callable.call()`，可以确保被代理的方法逻辑会被执行，而且可以在调用前后插入额外的处理逻辑

3. `net.bytebuddy.implementation.bind.annotation.AllArguments`

`@AllArguments`注解允许将被拦截方法的所有参数作为一个数组传递给拦截器方法

在需要访问或记录所有输入参数的场合特别有用，而不必分别为每个参数定义单独的注解。

```java
 public static void logArguments(@AllArguments Object[] args) {
    System.out.println("Arguments passed: " + Arrays.toString(args));
}
```

5. `net.bytebuddy.implementation.bind.annotation.Argument`

`@Argument`注解允许直接访问方法的第n个参数（从0开始计数）。这在你只需要特定参数时非常方便，避免了处理整个参数数组的需要。

```java
public class SpecificArgumentInterceptor {
    public static void logFirstArgument(@Argument(0) String firstArg) {
        System.out.println("First argument: " + firstArg);
    }
}
```
### 其他常用注解

| 注解            | 	说明                                                                                                                                                                                    |
|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| @Argument	    | 绑定单个参数                                                                                                                                                                                 |
| @AllArguments | 	绑定所有参数的数组                                                                                                                                                                             |
| @This	        | 当前被拦截的、动态生成的那个对象                                                                                                                                                                       |
| @Super	       | 当前被拦截的、动态生成的那个对象的父类对象                                                                                                                                                                  |
| @Origin	      | 可以绑定到以下类型的参数：</br> Method 被调用的原始方法</br>  Constructor 被调用的原始构造器</br>  Class 当前动态创建的类</br>  MethodHandle</br>  MethodType</br>  String </br> 动态类的toString()的返回值 </br> int</br>  动态方法的修饰符 |
| @DefaultCall	 | 调用默认方法而非super的方法                                                                                                                                                                       |
| @SuperCall	   | 用于调用父类版本的方法                                                                                                                                                                            |
| @Super	       | 注入父类型对象，可以是接口，从而调用它的任何方法                                                                                                                                                               |
| @RuntimeType	 | 可以用在返回值、参数上，提示ByteBuddy禁用严格的类型检查                                                                                                                                                       |
| @Empty	       | 注入参数的类型的默认值                                                                                                                                                                            |
| @StubValue	   | 注入一个存根值。<br/>对于返回引用、void的方法，注入null；<br/>对于返回原始类型的方法，注入0                                                                                                                                |
| @FieldValue   | 	注入被拦截对象的一个字段的值                                                                                                                                                                        |
| @Morph        | 	类似于@SuperCall，但是允许指定调用参数                                                                                                                                                              |

## 关键 API

`ByteBuddy`
- 流式API方式的入口类。
- 提供Subclassing/Redefining/Rebasing方式改写字节码。
- 所有的操作依赖DynamicType.Builder进行，创建不可变的对象。

`ElementMatchers(ElementMatcher)`
- 提供一系列的元素匹配的工具类（named/any/nameEndsWith等）。
- ElementMatcher提供对类型、方法、字段、注解进行matches的方式，类似于Predicate。
- Junction对多个ElementMatcher进行了and/or操作。

`DynamicType`（动态类型，所有字节码操作的开始，非常值得关注）：
- Unloaded（动态创建的字节码还未加载进入到底虚拟机，需要类加载器进行加载）。
- Loaded（已加载到vm中后，解析出Class表示）。
- Default（DynamicType的默认实现，完成相关实际操作）。


`Implementation`（用于提供动态方法的实现）：
- FixedValue（方法调用返回固定值）。
- MethodDelegation（方法调用委托，支持两种方式：Class的static方法调用、object的instance method方法调用）

`Builder`（用于创建DynamicType，相关接口以及实现后续待详解）：
- MethodDefinition
- FieldDefinition
- AbstractBase

