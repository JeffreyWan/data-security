package com.tulingframework.security.web.exception;

import org.zj.framework.exceptions.AbstractException;

/**
 * Created by wan on 15/01/2018.
 */
public class SignVerifyException extends AbstractException {

    public SignVerifyException(String msg) {
        super(msg);
    }

    public SignVerifyException(String msg, Throwable e) {
        super(e, 500, msg);
    }

    public SignVerifyException(String errorMessage, String msg) {
        super(errorMessage, msg);
    }
}
