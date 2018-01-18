package com.tulingframework.security.web.annotation;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.*;

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
        Assert.notNull(requestMappingInfo, "加密初始化失败");
        Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
        if (CollectionUtils.isEmpty(methods)) {
            methods = new HashSet<>(Arrays.asList(RequestMethod.values()));
        }
        if (!CollectionUtils.isEmpty(requestMappingInfo.getPatternsCondition().getPatterns())) {
            for (String path : requestMappingInfo.getPatternsCondition().getPatterns()) {
                for (RequestMethod method : methods) {
                    put(String.format(REQUEST_INFO, path, method.name()), value);
                }
            }
            return value;
        }
        return null;
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
