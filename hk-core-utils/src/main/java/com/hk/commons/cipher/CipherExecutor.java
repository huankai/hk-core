package com.hk.commons.cipher;

/**
 * 密码解析器
 *
 * @author huangkai
 * @date 2019-05-08 22:55
 */
public interface CipherExecutor<I, O> {

    /**
     * @param value value
     * @return
     */
    default O encode(final I value) {
        return encode(value, new Object[]{});
    }

    /**
     * @param value
     * @param parameters
     * @return
     */
    O encode(I value, Object[] parameters);

    /**
     * @param value
     * @param parameters
     * @return
     */
    O decode(I value, Object[] parameters);

    /**
     * Decode the value.
     *
     * @param value the value
     * @return the decoded value or null
     */
    default O decode(final I value) {
        return decode(value, new Object[]{});
    }
}
