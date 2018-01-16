package com.tulingframework.security.web.filter.wrapper;

import com.tulingframework.security.web.exception.SignVerifyException;
import com.tulingframework.security.web.filter.wrapper.provider.SecretProvider;
import com.tulingframework.security.web.filter.wrapper.provider.SignSecretProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Created by wan on 15/01/2018.
 */
@Slf4j
public class SignRequestSecurityOperator extends AbstractRequestSecurityFactory implements RequestSecurityOperator {

    @Override
    public synchronized ServletRequestWrapper getRequestWrapper(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request) {
            private Map<String, String[]> parameters;
            @Override
            public String[] getParameterValues(String name) {
                parameters = new HashMap<>(getParameterMap());
                if (!parameters.containsKey("sign") || ArrayUtils.isEmpty(parameters.get("sign"))) {
                    throw new SignVerifyException("签名不能为空");
                } else {
                    String sign = parameters.get("sign")[0];
                    parameters.remove("sign");
                    if (log.isDebugEnabled()) {
                        log.debug("开始进行签名校验,接收签名值:[{}],实际签名值:[{}]", sign, sign(parameters));
                    }
                    if (!sign.equals(sign(parameters))) {
                        throw new SignVerifyException("签名校验失败");
                    }
                }
                return parameters.get(name);
            }
        };
    }

    @Override
    public synchronized MappingJackson2HttpMessageConverter getMessageConverter(HttpServletRequest request) {
        return null;
    }

    @Override
    public SecretProvider getSecretProvider() {
        if (provider == null) {
            return new SignSecretProvider();
        }
        return provider;
    }

    public void setSecretProvider(SecretProvider provider) {
        this.provider = provider;
    }

    private String sign(Map<String, String[]> params) {
        if (CollectionUtils.isEmpty(params)) {
            params = new HashMap<>();
        }
        List<String> kvs = toLowerCaseList(params);
        Collections.sort(kvs);
        String paramString = StringUtils.join(kvs, "&");
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            throw new SignVerifyException("请求为传递token", "签名失败");
        }
        paramString = paramString.concat(getSecretProvider().provideSecret(token));
        return DigestUtils.md5Hex(paramString).toUpperCase();
    }

    private static List<String> toLowerCaseList(Map<String, String[]> parameters) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String[] entryValue = entry.getValue();
            if (ArrayUtils.isEmpty(entryValue)) {
                continue;
            }
            result.add(entry.getKey().toLowerCase().concat("=")  + (entryValue.length > 1 ? Arrays.asList(entryValue) : entryValue[0]));
        }
        return result;
    }

}
