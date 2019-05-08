package com.hk.commons.cipher;

import com.hk.commons.util.Base64Utils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * base64
 *
 * @author huangkai
 * @date 2019-05-08 23:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64CipherExecutor implements CipherExecutor<Serializable, String> {

    private static CipherExecutor<Serializable, String> INSTANCE;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CipherExecutor<Serializable, String> getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Base64CipherExecutor();
        }
        return INSTANCE;
    }

    @Override
    public String encode(Serializable value, Object[] parameters) {
        return Base64Utils.encodeToString(value.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decode(Serializable value, Object[] parameters) {
        return new String(Base64Utils.decode(value.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
