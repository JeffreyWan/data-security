package com.tulingframework.security.web.annotation;

import java.lang.annotation.*;

/**
 * Created by wan on 12/01/2018.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSecurity {

    SecretType[] types() default SecretType.SIGN;

}
