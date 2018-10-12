package com.hk.message.weixin.publish;

import com.hk.commons.util.AssertUtils;
import com.hk.message.api.subject.MessageSubject;
import com.hk.message.api.publish.MessagePublish;
import com.hk.message.weixin.WeixinTemplateMessageSubject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: huangkai
 * @date: 2018-9-23 13:09
 */
public class WeixinTemplateMessagePublish implements MessagePublish {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinTemplateMessagePublish.class);

    private WxMpTemplateMsgService wxMpTemplateMsgService;

    private WeixinTemplateMessageSubject messageSubject;

    public WeixinTemplateMessagePublish(WxMpTemplateMsgService wxMpTemplateMsgService) {
        this.wxMpTemplateMsgService = wxMpTemplateMsgService;
    }

    @Override
    public MessagePublish to(MessageSubject subject) {
        AssertUtils.isTrue(WeixinTemplateMessageSubject.class.isAssignableFrom(subject.getClass()), "请使用 WeixinTemplateMessageSubject构建");
        this.messageSubject = (WeixinTemplateMessageSubject) subject;
        return this;
    }

    @Override
    public void send() {
        try {
            wxMpTemplateMsgService.sendTemplateMsg(messageSubject.getTemplateMessage());
        } catch (WxErrorException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("发送模板消息失败:", e);
            }

        }
    }
}
