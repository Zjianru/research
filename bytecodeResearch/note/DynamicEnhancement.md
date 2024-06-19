# bytebuddy 03 - 动态增强

可对返回值、参数进行处理
在处理返回值前后，可指定运行代码，达到拦截的目的

拦截方法的规则可自定义，写法可实现嵌套

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

