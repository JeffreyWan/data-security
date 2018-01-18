package com.tulingframework.security.web.filter.wrapper;

import com.tulingframework.security.web.filter.wrapper.provider.SecretProvider;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wan on 17/01/2018.
 */
public interface DecryptServletRequestWrapper extends HttpServletRequest {

    DecryptServletRequestWrapper decrypt();

    SecretProvider getSecretProvider();

}
