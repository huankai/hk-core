package com.hk.core.authentication.api.validatecode;

import com.hk.commons.sms.SmsSender;
import com.hk.commons.util.AssertUtils;
import lombok.Setter;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

/**
 * @author kevin
 * @date 2019-8-8 15:19
 */
public class SimpleSmsCodeProcessor extends AbstractSmsValidateCodeProcessor {

    @Setter
    private String message;

    public SimpleSmsCodeProcessor(SmsSender<?> smsSender, String message) {
        super(smsSender);
        AssertUtils.notEmpty(message, "发送消息不能为空");
        this.message = message;
    }

    @Override
    protected void doSend(String phone, ValidateCode validateCode, ServletWebRequest request) throws IOException {
        smsSender.sendSms(phone, message);
    }
}
