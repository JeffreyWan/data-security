package com.tulingframework.security.web.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by wan on 10/01/2018.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DataSecurityConfiguration.class})
public @interface EnableDataSecurity {

}
