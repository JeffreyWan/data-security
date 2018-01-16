package com.tulingframework.security.web.exception;

import org.zj.framework.exceptions.AbstractException;

/**
 * Created by wan on 15/01/2018.
 */
public class MissSecretTypeException extends AbstractException {

    public MissSecretTypeException(String msg) {
        super(606, msg);
    }
}
