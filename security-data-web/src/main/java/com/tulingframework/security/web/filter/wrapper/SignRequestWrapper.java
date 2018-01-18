package com.tulingframework.security.web.filter.wrapper;

import com.tulingframework.security.web.exception.SignVerifyException;
import com.tulingframework.security.web.filter.wrapper.provider.SecretProvider;
import com.tulingframework.security.web.filter.wrapper.provider.SignSecretProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Created by wan on 17/01/2018.
 */
@Slf4j
public class SignRequestWrapper extends HttpServletRequestWrapper implements DecryptServletRequestWrapper {

    public SignRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public DecryptServletRequestWrapper decrypt() {
        Map<String, String[]> parameters = new HashMap<>(getParameterMap());
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
        return this;
    }

    @Override
    public SecretProvider getSecretProvider() {
        return new SignSecretProvider();
    }

    private String sign(Map<String, String[]> params) {
        if (CollectionUtils.isEmpty(params)) {
            params = new HashMap<>();
        }
        List<String> kvs = toLowerCaseList(params);
        Collections.sort(kvs);
        String paramString = StringUtils.join(kvs, "&");
        String token = ((HttpServletRequest)getRequest()).getHeader("token");
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
            if (ArrayUtils.isEmpty(entryValue) || isAllBlank(entryValue)) {
                continue;
            }
            result.add(entry.getKey().toLowerCase().concat("=") + (entryValue.length > 1 ? Arrays.asList(entryValue) : entryValue[0]));
        }
        return result;
    }

    private static boolean isAllBlank(String[] as) {
        for (String a : as) {
            if (StringUtils.isNotBlank(a)) {
                return false;
            }
        }
        return true;
    }
}
