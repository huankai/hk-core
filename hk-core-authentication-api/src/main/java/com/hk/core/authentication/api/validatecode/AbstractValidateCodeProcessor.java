package com.hk.core.authentication.api.validatecode;

import com.hk.commons.util.StringUtils;
import com.hk.core.web.Webs;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: kevin
 * @date 2018-07-27 13:44
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 验证码生成器
     */
    private ValidateCodeGenerator<C> validateCodeGenerator;

    /**
     * 验证码存储器
     */
    private ValidateCodeStrategy validateCodeStrategy = InMemoryValidateCodeStrategy.INSTANCE;

    /**
     * 验证码请求参数名
     */
    private final String codeParameterName;

    protected AbstractValidateCodeProcessor(ValidateCodeGenerator<C> validateCodeGenerator, String codeParameterName) {
        this.validateCodeGenerator = validateCodeGenerator;
        this.codeParameterName = codeParameterName;
    }

    protected void setValidateCodeStrategy(ValidateCodeStrategy validateCodeStrategy) {
        this.validateCodeStrategy = validateCodeStrategy;
    }

    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = doCreate(request);
        saveValidateCode(validateCode, request);
        send(validateCode, request);
    }

    /**
     * 发送验证码
     *
     * @param validateCode 验证码
     * @param request      request
     */
    protected abstract void send(C validateCode, ServletWebRequest request) throws Exception;

    /**
     * 保存生成的验证码
     *
     * @param validateCode 验证码
     * @param request      request
     */
    protected void saveValidateCode(C validateCode, ServletWebRequest request) {
        validateCodeStrategy.save(request, getStoreKey(request), validateCode);
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 验证码存储的key
     *
     * @param request request
     * @return
     */
    private String getStoreKey(ServletWebRequest request) {
        return VALIDATE_CODE_PREFIX + getName() + Webs.getRemoteAddr(request.getRequest());
    }

    /**
     * 创建验证码
     *
     * @param request request
     * @return 生成的验证码
     */
    protected C doCreate(ServletWebRequest request) {
        return validateCodeGenerator.generate();
    }

    /**
     * 验证验证码
     *
     * @param request request
     */
    @Override
    @SuppressWarnings("unchecked")
    public void validate(ServletWebRequest request) throws ValidateCodeException {
        final String key = getStoreKey(request);
        C inStoreValidateCode = (C) validateCodeStrategy.get(request, key);
        if (null == inStoreValidateCode) {
            throw new ValidateCodeException("验证码不存在");
        }
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeParameterName);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        if (inStoreValidateCode.isExpired()) {
            validateCodeStrategy.remove(request, key);
            throw new ValidateCodeException("验证码已过期");
        }
        if (StringUtils.notEquals(inStoreValidateCode.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        validateCodeStrategy.remove(request, key);
    }
}