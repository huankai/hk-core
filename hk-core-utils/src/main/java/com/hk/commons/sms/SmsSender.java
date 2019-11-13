package com.hk.commons.sms;

import com.hk.commons.JsonResult;
import com.hk.commons.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author kevin
 * @date 2019-8-10 14:24
 */
public interface SmsSender<R> {

    /**
     * 短信发送
     *
     * @param phone   手机号
     * @param message 消息内容
     * @return 结果
     * @throws IOException
     */
    default JsonResult<R> sendSms(String phone, String message) throws IOException {
        return sendSms(StringUtils.commaDelimitedListToSet(phone), message);
    }

    /**
     * @param phones  手机号
     * @param message 消息内容
     * @return 结果
     * @throws IOException
     */
    JsonResult<R> sendSms(Set<String> phones, String message) throws IOException;

    /**
     * @param phone             手机号
     * @param smsTemplateId     模板 id
     * @param templateParameter 消息内容
     * @return 结果
     * @throws IOException
     */
    default JsonResult<R> sendTemplateSms(String phone, String smsTemplateId, Map<String, ?> templateParameter) throws IOException {
        return sendTemplateSms(StringUtils.commaDelimitedListToSet(phone), smsTemplateId, templateParameter);
    }

    /**
     * @param phones            手机号
     * @param smsTemplateId     模板 id
     * @param templateParameter 模板参数
     * @return 结果
     * @throws IOException
     */
    JsonResult<R> sendTemplateSms(Set<String> phones, String smsTemplateId, Map<String, ?> templateParameter) throws IOException;
}
