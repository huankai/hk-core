package com.hk.core.authentication.api.validatecode;

import com.hk.commons.sms.SmsSender;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import lombok.Setter;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-8-8 15:23
 */
public class TemplateSmsCodeProcessor extends AbstractSmsValidateCodeProcessor {

    private static final String TEMPLATE_CODE_KEY = "code";

    private static final String TEMPLATE_expire_KEY = "expire";

    private final String templateId;

    @Setter
    private String codeKey = TEMPLATE_CODE_KEY;

    @Setter
    private String expireKey = TEMPLATE_expire_KEY;

    @Setter
    private Map<String, Object> templateParameter;

    public TemplateSmsCodeProcessor(String templateId, SmsSender<?> smsSender) {
        super(smsSender);
        this.templateId = templateId;
    }

    @Override
    protected void doSend(String phone, ValidateCode validateCode, ServletWebRequest request) throws IOException {
        Map<String, Object> templateMap = new HashMap<>(2);
        templateMap.put(codeKey, validateCode.getCode());
        templateMap.put(expireKey, validateCode.getExpireSecond() / 60);// 秒转分钟
        if (CollectionUtils.isNotEmpty(this.templateParameter)) {
            templateMap.putAll(this.templateParameter);
        }
        smsSender.sendTemplateSms(StringUtils.commaDelimitedListToSet(phone), templateId, templateMap);
    }
}
