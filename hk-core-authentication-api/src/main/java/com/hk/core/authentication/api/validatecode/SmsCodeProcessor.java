package com.hk.core.authentication.api.validatecode;

import com.hk.commons.sms.SmsCodeSender;
import com.hk.commons.util.AssertUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码发送
 *
 * @author kevin
 * @date 2018-07-27 14:38
 */
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    /**
     * 短信验证码发送器
     */
    private SmsCodeSender smsCodeSender;

    private String mobileParamName;

    public SmsCodeProcessor(ValidateCodeGenerator<ValidateCode> validateCodeGenerator, SmsCodeSender smsCodeSender, String mobileParamName, String codeParameterName) {
        super(validateCodeGenerator, codeParameterName);
        this.mobileParamName = mobileParamName;
        this.smsCodeSender = smsCodeSender;
    }

    @Override
    protected void send(ValidateCode validateCode, ServletWebRequest request) throws ServletRequestBindingException {
        String phone = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), mobileParamName);
        AssertUtils.hasText(phone,"手机号不能为空");
        smsCodeSender.send(phone, validateCode.getCode(), validateCode.getExpireSecond());
    }
}
