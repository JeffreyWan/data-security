package com.tulingframework.security.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tulingframework.security.web.annotation.DataSecurity;
import com.tulingframework.security.web.annotation.RequestPathMapping;
import com.tulingframework.security.web.annotation.SecretType;
import com.tulingframework.security.web.exception.SecretIllegalArgumentException;
import com.tulingframework.security.web.filter.wrapper.RequestSecurityOperator;
import com.tulingframework.security.web.filter.wrapper.SignRequestSecurityOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;
import org.zj.framework.exceptions.AbstractException;
import org.zj.framework.exceptions.NormalException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wan on 15/01/2018.
 */
@Slf4j
public class DataSecurityFilter extends OncePerRequestFilter implements SecurityFilter {

    private static final Map<SecretType, RequestSecurityOperator> refs = new HashMap<>(2);

    private static final String TYPE = "secret-type";

    private RequestPathMapping pathMapping;

    public DataSecurityFilter(final RequestMappingHandlerMapping handlerMapping) {
        this.pathMapping = build(handlerMapping);
        refs.put(SecretType.SIGN, new SignRequestSecurityOperator());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = new UrlPathHelper().getOriginatingServletPath(request);
        String method = request.getMethod().toUpperCase();
        if (!pathMapping.containsCompositeKey(path, method)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            SecretType secretType = parseType(request);
            if (!hasRequestBody(pathMapping.get(path, method))) {
                if (!refs.containsKey(secretType)) {
                    throw new SecretIllegalArgumentException("can not find data security operators");
                }
                chain.doFilter(refs.get(secretType).createParameterRequestWrapper(request), response);
            } else {
                //TODO has requestBody in method
                chain.doFilter(request, response);
            }
        } catch (RuntimeException e) {
            writeErrorResponse(response, e);
        }
    }

    public void setRequestMappingHandlerMapping(RequestMappingHandlerMapping handlerMapping) {
        this.pathMapping = build(handlerMapping);
    }

    @Override
    public void addRef(SecretType type, RequestSecurityOperator operator) {
        refs.put(type, operator);
    }

    private RequestPathMapping build(final RequestMappingHandlerMapping handlerMapping) {
        Assert.notNull(handlerMapping, "接口映射集合不能为空");
        return new RequestPathMapping() {{
            for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMapping.getHandlerMethods().entrySet()) {
                if (entry.getValue().getMethod().getAnnotation(DataSecurity.class) != null ||
                        entry.getValue().getMethod().getDeclaringClass().getAnnotation(DataSecurity.class) != null) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }};
    }

    private boolean hasRequestBody(HandlerMethod handlerMethod) {
        Annotation[][] parameterAnnotations = handlerMethod.getMethod().getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return false;
        }
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestBody) {
                    return true;
                }
            }
        }
        return false;
    }


    private SecretType parseType(HttpServletRequest request) {
        return SecretType.typeOf(request.getHeader(TYPE));
    }

    private void writeErrorResponse(ServletResponse response, RuntimeException e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        if (e instanceof AbstractException) {
            out.print(mapper.writeValueAsString(e));
        } else {
            out.print(mapper.writeValueAsString(new NormalException(e.getMessage())));
        }
        out.flush();
        out.close();
    }

}
