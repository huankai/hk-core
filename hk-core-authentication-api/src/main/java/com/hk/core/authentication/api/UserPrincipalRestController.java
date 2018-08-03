package com.hk.core.authentication.api;

import com.hk.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: kevin
 * @date 2018-08-01 16:48
 */
@RestController
public class UserPrincipalRestController extends BaseController {

    @Autowired
    private SecurityContext securityContext;

    /**
     * 获取当前登陆的用户信息
     *
     * @return
     */
    @GetMapping("/current_user")
    public UserPrincipal getCurrentUser() {
        return securityContext.getPrincipal();
    }
}
