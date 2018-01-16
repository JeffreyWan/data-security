package com.tulingframework.security.web.annotation;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wan on 15/01/2018.
 */
public class RequestPathMapping extends HashMap<String, HandlerMethod> {

    private final static String REQUEST_INFO = "{[%s],methods=[%s]}";

    public HandlerMethod get(String path, String method) {
        return super.get(String.format(REQUEST_INFO, path, method.toUpperCase()));
    }

    public HandlerMethod put(String path, String method, HandlerMethod value) {
        return super.put(String.format(REQUEST_INFO, path, method.toUpperCase()), value);
    }

    public HandlerMethod put(RequestMappingInfo requestMappingInfo, HandlerMethod value) {
        return super.put(requestMappingInfo.toString(), value);
    }

    public RequestPathMapping putAllFrom(Map<RequestMappingInfo, HandlerMethod> m) {
        for (Entry<RequestMappingInfo, HandlerMethod> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public boolean containsCompositeKey(String path, String method) {
        return super.containsKey(String.format(REQUEST_INFO, path, method.toUpperCase()));
    }

}
