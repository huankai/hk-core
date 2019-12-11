package com.hk.commons;

import com.hk.commons.annotations.EnumDisplay;
import com.hk.commons.util.EnumDisplayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangkai
 * @date 2019-07-29 22:02
 */
public enum Status {

    /**
     * 成功
     * {@link #SUCCESS_STATUS}
     */
    @EnumDisplay(value = "operation.success", order = 10200)
    SUCCESS,

    /**
     * 失败
     */
    @EnumDisplay(value = "operation.failure", order = -1)
    FAILURE,

    /**
     * 重定向
     */
    @EnumDisplay(value = "operation.redirect", order = 10302)
    REDIRECT,

    /**
     * 坏的请求
     */
    @EnumDisplay(value = "operation.bad_request", order = 10400)
    BAD_REQUEST,

    /**
     * 未认证
     */
    @EnumDisplay(value = "operation.unauthorized", order = 10401)
    UNAUTHORIZED,

    /**
     * 无权限
     */
    @EnumDisplay(value = "operation.forbidden", order = 10403)
    FORBIDDEN,

    /**
     * 资源不存在
     */
    @EnumDisplay(value = "operation.not_found", order = 10404)
    NOT_FOUND,

    /**
     * 请求方法不支持
     */
    @EnumDisplay(value = "operation.method_not_allowed", order = 10405)
    METHOD_NOT_ALLOWED,

    /**
     * 服务器错误
     */
    @EnumDisplay(value = "operation.server_error", order = 10500)
    SERVER_ERROR;

    /**
     * 成功状态码
     */
    static final int SUCCESS_STATUS = 10200;

    private static final Map<Status, EnumDisplayUtils.EnumItem> map;

    static {
        map = new HashMap<>(Status.values().length);
        EnumDisplayUtils.EnumItem enumItem;
        for (var item : Status.values()) {
            var enumDisplay = EnumDisplayUtils.getEnumDisplay(item);
            if (null != enumDisplay) {
                enumItem = new EnumDisplayUtils.EnumItem();
                enumItem.setOrder(enumDisplay.order());
                enumItem.setValue(enumDisplay.value());
                map.put(item, enumItem);
            }
        }
    }

    public static int getOrder(Status status) {
        return map.get(status).getOrder();
    }

    public static String getMessage(int code) {
        return map.values().stream()
                .filter(item -> item.getOrder() == code)
                .findFirst()
                .map(find -> find.getValue().toString())
                .orElse(null);
    }
}
