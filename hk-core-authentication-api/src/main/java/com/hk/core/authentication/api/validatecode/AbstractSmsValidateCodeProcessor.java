package com.hk.core.authentication.api.validatecode;

import com.hk.commons.sms.SmsSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 短信验证码发送
 *
 * @author kevin
 * @date 2018-07-27 14:38
 */
public abstract class AbstractSmsValidateCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    private static final String DEFAULT_PHONE_PARAM_NAME = "phoneNumber";

    /**
     * 短信验证码发送器
     */
    protected final SmsSender<?> smsSender;

    @Setter
    @Getter
    private String mobileParamName = DEFAULT_PHONE_PARAM_NAME;

    public AbstractSmsValidateCodeProcessor(SmsSender<?> smsSender) {
        super(new DefaultValidateCodeGenerator());
        this.smsSender = smsSender;
    }

    @Override
    protected final void send(ValidateCode validateCode, ServletWebRequest request)
            throws ServletRequestBindingException, IOException {
        HttpServletRequest req = request.getRequest();
        String phone = ServletRequestUtils.getRequiredStringParameter(req, mobileParamName);
        doSend(phone, validateCode, request);
    }

    protected abstract void doSend(String phone, ValidateCode validateCode, ServletWebRequest request)
            throws ServletRequestBindingException, IOException;

    @Override
    protected String getSuffix(ServletWebRequest request) throws ServletRequestBindingException {
        return ServletRequestUtils.getRequiredStringParameter(request.getRequest(), mobileParamName);
    }
}
