package kr.kro.colla.utils;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProxyInitialized {

    String proxyTarget() default "nothing";

}
