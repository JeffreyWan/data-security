package com.tulingframework.security.web.filter.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wan on 15/01/2018.
 */
@Slf4j
public class SignRequestSecurityOperator extends AbstractRequestSecurityFactory implements RequestSecurityOperator {

    @Override
    protected DecryptServletRequestWrapper instanceRequestWrapper(HttpServletRequest request) {
        return new SignRequestWrapper(request);
    }


}
