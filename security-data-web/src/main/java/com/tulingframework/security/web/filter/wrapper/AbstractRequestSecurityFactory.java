package com.tulingframework.security.web.filter.wrapper;

import com.tulingframework.security.web.filter.wrapper.provider.SecretProvider;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wan on 15/01/2018.
 */
public abstract class AbstractRequestSecurityFactory {

    protected SecretProvider provider;

    protected HttpServletRequest request;

    public abstract SecretProvider getSecretProvider();

    public abstract ServletRequestWrapper getRequestWrapper(HttpServletRequest request);

    public abstract MappingJackson2HttpMessageConverter getMessageConverter(HttpServletRequest request);

    public ServletRequestWrapper createParameterRequestWrapper(HttpServletRequest request) {
        this.request = request;
        return getRequestWrapper(request);
    }

    public MappingJackson2HttpMessageConverter createMessageConverter(HttpServletRequest request) {
        this.request = request;
        return getMessageConverter(request);
    }
}
