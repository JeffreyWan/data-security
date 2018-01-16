package com.tulingframework.security.web.filter.wrapper.provider;

/**
 * Created by wan on 15/01/2018.
 */
public class SignSecretProvider implements SecretProvider {

    @Override
    public String provideSecret(String token) {
        return token;
    }
}
