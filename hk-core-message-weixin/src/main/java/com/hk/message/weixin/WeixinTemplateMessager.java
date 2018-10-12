package com.hk.message.weixin;

import com.hk.message.api.Message;
import com.hk.message.api.Messager;
import com.hk.message.api.publish.MessagePublish;
import com.hk.message.weixin.publish.WeixinTemplateMessagePublish;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * @author: huangkai
 * @date: 2018-9-23 13:19
 */
public class WeixinTemplateMessager implements Messager {

    private WxMpService wxMpService;

    public WeixinTemplateMessager(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    @Override
    public MessagePublish publish(Message message) {
        return new WeixinTemplateMessagePublish(wxMpService.getTemplateMsgService());
    }
}
