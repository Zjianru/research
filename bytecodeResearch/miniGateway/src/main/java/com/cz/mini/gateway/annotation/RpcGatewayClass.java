package com.cz.mini.gateway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * code desc
 *
 * @author Zjianru
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcGatewayClass {
    String classDesc() default "";

    String alias() default "";

    String timeOut() default "";

}
