package com.hk.message.weixin;

import com.hk.message.api.subject.MessageSubject;
import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

/**
 * @author huangkai
 * @date 2018-9-23 13:12
 */
@Data
public class WeixinTemplateMessageSubject implements MessageSubject {

    private WxMpTemplateMessage templateMessage;
}
