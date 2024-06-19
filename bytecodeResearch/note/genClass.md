# bytebuddy 01 - 生成类
生成的类的命名规则：

在不指定命名策略的情况下，且父类为 JDK 中的类，生成的类名称为：

`.../net/bytebuddy/renamed/java/lang/Object$ByteBuddy$lJQa6qEk.class`

对于父类非 jDK 类，生成的类名称为：

`.../com/cz/bytebuddy/demo/test/pojo/User$ByteBuddy$sbRaraBh.class`

可指定添加命名策略：

在不指定命名策略的情况下，且父类为 JDK 中的类，生成的类名称为：

`.../net/bytebuddy/renamed/java/lang/Object$genByte$sIhtVgbJ.class`

对于父类非 jDK 类，生成的类名称为：

`.../com/cz/bytebuddy/demo/test/pojo/User$genByte$VjQSWD7Q.class`

可指定包名类名进行生成，且类名需要符合 Java 类命名规则
```java
DynamicType.Unloaded<User> unloadedPojoSpecifyConcreteClassName = new ByteBuddy()
        .subclass(User.class)
        .name("com.cz.gen.genUser")
        .make();
```

生成的类为 ： `/com/cz/gen/genUser.class`

如需要跳过检测，可关闭 `TypeValidation`
```java
       DynamicType.Unloaded<User> unloadedPojoSpecifyConcreteClassName = new ByteBuddy()
                .with(TypeValidation.of(false))
                .subclass(User.class)
                .name("com.cz.gen.genUser")
                .make();
```

生成指定路径的文件
```java
        byte[] classByteStream = unloadedPojoSpecifyConcreteClassName.getBytes();
        // 如果指定了具体的名字，这里需要显式指定路径，且路径与指定的类名相同
        // 还是会以指定的具体名字生成 class，但是类文件会以这里指定的文件名为准
        String genClassPath = path+"/com/cz/gen/selfGen/genUser.class";
        FileUtils.writeByteArrayToFile(new File(genClassPath), classByteStream);
```

向某 jar 包内添加一个类

```java
    unloadedPojoSpecifyConcreteClassName.inject(new File("jar package location url like aa/bb/xxx.jar"));
```