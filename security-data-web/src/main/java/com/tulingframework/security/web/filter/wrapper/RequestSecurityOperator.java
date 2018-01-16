package com.tulingframework.security.web.filter.wrapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wan on 15/01/2018.
 */
public interface RequestSecurityOperator {

    ServletRequestWrapper createParameterRequestWrapper(HttpServletRequest request);

    MappingJackson2HttpMessageConverter createMessageConverter(HttpServletRequest request);

}
