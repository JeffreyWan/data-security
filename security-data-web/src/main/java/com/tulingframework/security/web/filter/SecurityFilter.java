package com.tulingframework.security.web.filter;

import com.tulingframework.security.web.annotation.SecretType;
import com.tulingframework.security.web.filter.wrapper.RequestSecurityOperator;

import javax.servlet.Filter;

/**
 * Created by wan on 16/01/2018.
 */
public interface SecurityFilter extends Filter {

    void addRef(SecretType type, RequestSecurityOperator operator);

}
