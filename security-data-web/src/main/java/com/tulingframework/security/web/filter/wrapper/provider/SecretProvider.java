package com.tulingframework.security.web.filter.wrapper.provider;

/**
 * Created by wan on 15/01/2018.
 */
public interface SecretProvider {

    String provideSecret(String token);

}
