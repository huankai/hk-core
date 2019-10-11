package com.hk.commons.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-8-10 14:46
 */
public final class StaticTemplateMessageService implements TemplateMessageService {

    private static final Map<String, String> TEMPLATE_MAP;

    static {
        TEMPLATE_MAP = new HashMap<>();
        TEMPLATE_MAP.put("1", "【机构标识】注册，短信验证码为${code}，请在${expire}分钟内输入");
        TEMPLATE_MAP.put("2", "【机构标识】绑定手机，短信验证码为${code}，请在${expire}分钟内输入");
        TEMPLATE_MAP.put("3", "【机构标识】忘记密码短信验证，验证码为${code}，请在${expire}分钟内输入");
        TEMPLATE_MAP.put("4", "【机构标识】尊敬的消费者：修改支付密码，短信验证码为${code}，请在${code}分钟内输入");
        TEMPLATE_MAP.put("5", "【机构标识】尊敬的${mchtName}会员： 您好，本店/公司 举行${name}活动。活动内容 ${context}。活动时间：截止 ${expireDate} 。地址：${address} 联系电话：${phone}");
    }

    public static boolean add(String templateId, String templateContent) {
        if (TEMPLATE_MAP.containsKey(templateId)) {
            return false;
        }
        TEMPLATE_MAP.put(templateId, templateContent);
        return true;
    }

    @Override
    public String getTemplateContent(String templateId) {
        return TEMPLATE_MAP.get(templateId);
    }
}
