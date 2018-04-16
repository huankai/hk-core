package com.hk.core.authentication.security.web;

import com.hk.commons.fastjson.JsonUtils;
import com.hk.core.web.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: huangkai
 * @date 2018-04-16 16:32
 */
@RestController
public class LoginController {

    @GetMapping("login")
    public String login() {
        return JsonUtils.toJSONString(JsonResult.error("未认证的用户"));
    }
}
