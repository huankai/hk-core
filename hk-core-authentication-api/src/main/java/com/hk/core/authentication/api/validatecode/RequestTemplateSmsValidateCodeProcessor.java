package com.hk.core.authentication.api.validatecode;

import com.hk.commons.sms.SmsSender;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.JsonUtils;
import lombok.Setter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author kevin
 * @date 2019-8-10 17:47
 */
public class RequestTemplateSmsValidateCodeProcessor extends AbstractSmsValidateCodeProcessor {

    private static final String TEMPLATE_ID_PARAMETER_NAME = "templateId";

    private static final String TEMPLATE_PARAMETER_PARAMETER_NAME = "templateParameter";

    @Setter
    private String templateIdParameterName = TEMPLATE_ID_PARAMETER_NAME;

    @Setter
    private String templateParameterName = TEMPLATE_PARAMETER_PARAMETER_NAME;

    public RequestTemplateSmsValidateCodeProcessor(SmsSender<?> smsSender) {
        super(smsSender);
    }

    @Override
    protected void doSend(String phone, ValidateCode validateCode, ServletWebRequest request) throws IOException, ServletRequestBindingException {
        HttpServletRequest req = request.getRequest();
        AssertUtils.notEmpty(phone, "手机号不能为空");
        String templateId = ServletRequestUtils.getStringParameter(req, templateIdParameterName);
        AssertUtils.notEmpty(templateId, "模板id 不能为空");
        String templateParameter = ServletRequestUtils.getStringParameter(req, templateParameterName);
        smsSender.sendTemplateSms(phone, templateId, JsonUtils.deserializeMap(templateParameter, String.class, String.class));
    }


}
