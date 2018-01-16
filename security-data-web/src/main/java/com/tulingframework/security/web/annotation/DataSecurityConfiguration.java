package com.tulingframework.security.web.annotation;

import com.tulingframework.security.web.filter.DataSecurityFilter;
import com.tulingframework.security.web.filter.SecurityFilter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by wan on 11/01/2018.
 */
@Lazy
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class DataSecurityConfiguration {

    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    @Order
    public SecurityFilter dataSecurityFilter() {
        return new DataSecurityFilter(handlerMapping());
    }

}