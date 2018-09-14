package com.hk.weixin.qrcode;

import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.UserPrincipal;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信二维码回调过滤器
 *
 * @author: kevin
 * @date: 2018年2月8日上午11:18:31
 */
public class WechatQrCodeCallbackAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CODE_PARAM_NAME = "code";

    private static final String STATE_PARAM_NAME = "state";

    private final WxMpService wxService;

    private final WechatQrCodeConfig config;

    public WechatQrCodeCallbackAuthenticationFilter(WxMpService wxMpService, WechatQrCodeConfig config) {
        /*
         * 处理 微信回调的url请求
         */
        super(new AntPathRequestMatcher(config.getCallbackUrl()));
        this.wxService = wxMpService;
        this.config = config;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        /*
         * 微信二维码回调会有两个参数返回:
         * code 与 state
         * 查看文档 ：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=b2aec2aaa65154f7df33d39101e4aedb61d8fef3&lang=zh_CN
         */
        final String code = request.getParameter(CODE_PARAM_NAME);
        final String state = request.getParameter(STATE_PARAM_NAME);
        if (StringUtils.isNotEmpty(config.getState()) && StringUtils.notEquals(config.getState(), state)) {
            //csrf攻击（跨站请求伪造攻击）
            throw new AuthenticationServiceException("登录失败！");
        }
        if (StringUtils.isNotEmpty(code)) { // 用户同意授权
            try {
                WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
                WxMpUser mpUser = wxService.oauth2getUserInfo(accessToken, null);
                UserPrincipal principal = new UserPrincipal(mpUser.getOpenId(), mpUser.getNickname(), false, mpUser.getNickname(), ByteConstants.ZERO, null, null,
                        Byte.valueOf(String.valueOf(mpUser.getSexId())), mpUser.getHeadImgUrl());
                WechatQrCodeAuthenticationToken authenticationToken = new WechatQrCodeAuthenticationToken(principal);
                setDetails(request, authenticationToken);
                return getAuthenticationManager().authenticate(authenticationToken);
            } catch (WxErrorException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("微信认证失败：" + e.getMessage(), e);
                }
                throw new AuthenticationServiceException(e.getMessage());
            }
        }
        return null;
    }

    private void setDetails(HttpServletRequest request, WechatQrCodeAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
