package com.tulingframework.security.web.filter.wrapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wan on 15/01/2018.
 */
public abstract class AbstractRequestSecurityFactory {

    protected HttpServletRequest request;

    protected abstract DecryptServletRequestWrapper instanceRequestWrapper(HttpServletRequest request);

    public DecryptServletRequestWrapper createParameterRequestWrapper(HttpServletRequest request) {
        this.request = request;
        return instanceRequestWrapper(request).decrypt();
    }

    public MappingJackson2HttpMessageConverter createMessageConverter(HttpServletRequest request) {
        this.request = request;
        return null;
    }

}
