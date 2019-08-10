package com.hk.core.authentication.api.validatecode;

import com.hk.commons.util.Lazy;
import com.hk.commons.util.SpringContextHolder;
import com.hk.commons.util.StringUtils;
import lombok.Setter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

/**
 * @author kevin
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
    private final Lazy<ValidateCodeStrategy> validateCodeStrategy = Lazy.of(() -> SpringContextHolder.getBean(ValidateCodeStrategy.class));

    /**
     * 验证码请求参数名
     */
    @Setter
    private String codeParameterName = DEFAULT_CODE_PARAMETER_NAME;

    AbstractValidateCodeProcessor(ValidateCodeGenerator<C> validateCodeGenerator) {
        this.validateCodeGenerator = validateCodeGenerator;
    }

    @Override
    public String create(ServletWebRequest request) throws IOException, ServletRequestBindingException {
        C validateCode = validateCodeGenerator.generate();
        saveValidateCode(validateCode, request);
        send(validateCode, request);
        return validateCode.getCode();
    }

    /**
     * 发送验证码
     *
     * @param validateCode 验证码
     * @param request      request
     */
    protected abstract void send(C validateCode, ServletWebRequest request) throws IOException, ServletRequestBindingException;

    /**
     * 保存生成的验证码
     *
     * @param validateCode 验证码
     * @param request      request
     */
    protected void saveValidateCode(C validateCode, ServletWebRequest request) throws ServletRequestBindingException {
        validateCodeStrategy.get().save(request, getStoreKey(request), validateCode);
    }

    protected abstract String getSuffix(ServletWebRequest request) throws ServletRequestBindingException;

    /**
     * 验证码存储的key
     *
     * @param request request
     * @return
     */
    private String getStoreKey(ServletWebRequest request) throws ServletRequestBindingException {
        return VALIDATE_CODE_PREFIX.concat(getClass().getSimpleName()).concat(getSuffix(request));
    }

    /**
     * 验证验证码
     *
     * @param request request
     */
    @Override
    public void validate(ServletWebRequest request) throws ValidateCodeException, ServletRequestBindingException {
        final String key = getStoreKey(request);
        ValidateCodeStrategy strategy = this.validateCodeStrategy.get();
        C inStoreValidateCode = strategy.get(request, key);
        if (null == inStoreValidateCode) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (inStoreValidateCode.isExpired()) {
            strategy.remove(request, key);
            throw new ValidateCodeException("验证码已过期");
        }
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeParameterName);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        if (StringUtils.notEquals(inStoreValidateCode.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        strategy.remove(request, key);
    }
}
