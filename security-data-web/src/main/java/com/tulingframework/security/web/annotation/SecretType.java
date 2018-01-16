package com.tulingframework.security.web.annotation;

import com.tulingframework.security.web.exception.SecretIllegalArgumentException;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wan on 15/01/2018.
 */
public enum SecretType {

    SIGN;

    public static SecretType typeOf(String type) {
        if (StringUtils.isBlank(type)) {
            throw new SecretIllegalArgumentException("missing secret type");
        }
        for (SecretType secretType : SecretType.values()) {
            if (type.equalsIgnoreCase(secretType.name())) {
                return secretType;
            }
        }
        throw new SecretIllegalArgumentException("not fond SecretType type of [" + type + "]");
    }

}
