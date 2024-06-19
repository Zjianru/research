package com.cz.bytebuddy.demo;

import com.cz.bytebuddy.demo.pojo.User;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * code desc
 *
 * @author Zjianru
 */
public class CreateClass {

    public static void main(String[] args) {

        String path = CreateClass.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        // gen class with concrete parent class
        genClassWithConcreteParentClass(path);

        // gen class with namingStrategy
        genClassWithNamingStrategy(path);

        // gen class with concrete className and type validation
        genClassWithConcreteClassNameAndTypeValidation(path);

        // gen class with concrete className and write class to clear file
        genClassWithConcreteClassNameAndWriteClassToClearFile(path);

    }

    private static void genClassWithConcreteClassNameAndWriteClassToClearFile(String path) {
        DynamicType.Unloaded<User> unloadedPojoSpecifyConcreteClassName = new ByteBuddy().subclass(User.class).name("com.cz.gen.genUser").make();
        try {
            unloadedPojoSpecifyConcreteClassName.saveIn(new File(path));
            byte[] classByteStream = unloadedPojoSpecifyConcreteClassName.getBytes();
            // 如果指定了具体的名字，这里需要显式指定路径，且路径与指定的类名相同
            // 还是会以指定的具体名字生成 class，但是类文件会以这里指定的文件名为准
            String genClassPath = path + "/com/cz/gen/selfGen/genUser.class";
            FileUtils.writeByteArrayToFile(new File(genClassPath), classByteStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void genClassWithConcreteClassNameAndTypeValidation(String path) {
        DynamicType.Unloaded<User> unloadedPojoSpecifyConcreteClassName = new ByteBuddy().subclass(User.class).name("com.cz.gen.genUser").make();

        DynamicType.Unloaded<User> unloadedPojoSpecifyConcreteClassNameWithOutTypeValidation = new ByteBuddy().with(TypeValidation.of(false)).subclass(User.class).name("com.cz.gen.genUser").make();
        try {
            unloadedPojoSpecifyConcreteClassName.saveIn(new File(path));
            unloadedPojoSpecifyConcreteClassNameWithOutTypeValidation.saveIn(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void genClassWithNamingStrategy(String path) {
        NamingStrategy.SuffixingRandom namingStrategy = new NamingStrategy.SuffixingRandom("genByte");
        DynamicType.Unloaded<Object> unloadedObjectByNameStrategy = new ByteBuddy().with(namingStrategy).subclass(Object.class).make();
        DynamicType.Unloaded<User> unloadedPojoByNameStrategy = new ByteBuddy().with(namingStrategy).subclass(User.class).make();
        try {
            // .../net/bytebuddy/renamed/java/lang/Object$genByte$sIhtVgbJ.class
            unloadedObjectByNameStrategy.saveIn(new File(path));
            // .../com/cz/bytebuddy/demo/test/pojo/User$genByte$VjQSWD7Q.class
            unloadedPojoByNameStrategy.saveIn(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void genClassWithConcreteParentClass(String path) {
        try {

            // Unloaded 标识生成的类未加载到 jvm
            DynamicType.Unloaded<User> unloadedPojo = new ByteBuddy().subclass(User.class).make();
            DynamicType.Unloaded<Object> unloadedObject = new ByteBuddy().subclass(Object.class).make();
            // .../net/bytebuddy/renamed/java/lang/Object$ByteBuddy$lJQa6qEk.class
            unloadedObject.saveIn(new File(path));
            // .../com/cz/bytebuddy/demo/test/pojo/User$ByteBuddy$sbRaraBh.class
            unloadedPojo.saveIn(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
