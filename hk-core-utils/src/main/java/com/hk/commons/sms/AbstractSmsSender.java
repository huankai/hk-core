package com.hk.commons.sms;

import com.hk.commons.JsonResult;
import com.hk.commons.util.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author kevin
 * @date 2019-8-10 14:37
 */
public abstract class AbstractSmsSender<R> implements SmsSender<R> {

    private static Lazy<TemplateMessageService> templateMessageService = Lazy.of(() -> SpringContextHolder.getBean(TemplateMessageService.class));

    @Override
    public final JsonResult<R> sendSms(Set<String> phones, String message) throws IOException {
        validatePhone(phones);
        AssertUtils.notEmpty(message, "发送消息不能为空");
        return doSendSms(phones, message);
    }

    @Override
    public final JsonResult<R> sendTemplateSms(Set<String> phones, String smsTemplateId, Map<String, ?> templateParameter) throws IOException {
        validatePhone(phones);
        return doSendSms(phones, StringUtils.processTemplate(templateMessageService.get().getTemplateContent(smsTemplateId), templateParameter));
    }

    protected abstract JsonResult<R> doSendSms(Set<String> phones, String message) throws IOException;

    private void validatePhone(Collection<String> phones) {
        AssertUtils.notNull(phones, "验证手机号不能为空");
        for (String phone : phones) {
            AssertUtils.isTrue(ValidateUtils.isMobilePhone(phone), "手机号格式不正确:" + phone);
        }
    }
}
