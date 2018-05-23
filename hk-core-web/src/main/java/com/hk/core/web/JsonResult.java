package com.hk.core.web;

import com.hk.commons.annotations.EnumDisplay;
import com.hk.commons.util.EnumDisplayUtils;
import com.hk.commons.util.StringUtils;

/**
 * Json返回结果
 *
 * @author huangkai
 * @date 2017年9月27日上午11:09:08
 */
public final class JsonResult {

    public enum Status {

        @EnumDisplay(value = "operation.success", order = 10200)
        SUCCESS,

        @EnumDisplay(value = "operation.failure", order = -1)
        FAILURE,

        @EnumDisplay(value = "operation.redirect", order = 10302)
        REDIRECT,

        @EnumDisplay(value = "operation.bad_request", order = 10400)
        BAD_REQUEST,

        @EnumDisplay(value = "operation.unauthorized", order = 10401)
        UNAUTHORIZED,

        @EnumDisplay(value = "operation.not_found", order = 10404)
        NOT_FOUND,

        @EnumDisplay(value = "operation.method_not_allowed", order = 10405)
        METHOD_NOT_ALLOWED,

        @EnumDisplay(value = "operation.server_error", order = 10500)
        SERVER_ERROR

//        private int status;
//
//        private String message;
//
//        Status(int status, String message) {
//            this.status = status;
//            this.message = message;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public String getMessage() {
//            return message;
//        }
    }

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回状态
     */
    private Status status;

    /**
     * 返回消息信息
     */
    private String message;

    /**
     * 请求成功
     *
     * @return
     */
    public static JsonResult success() {
        return success(null);
    }

    public static JsonResult success(String message) {
        return new JsonResult(true, message);
    }

    /**
     * 请求成功
     *
     * @return
     */
    public static JsonResult success(Object data) {
        return new JsonResult(data);
    }

    /**
     * 请求失败
     *
     * @return
     */
    public static JsonResult failure() {
        return new JsonResult(Status.FAILURE);
    }

    /**
     * 请求失败
     *
     * @param message 失败信息
     * @return
     */
    public static JsonResult failure(String message) {
        return new JsonResult(false, message);
    }

    /**
     * 请求不正确
     *
     * @param message
     * @return
     */
    public static JsonResult badRueqest(String message) {
        return new JsonResult(Status.BAD_REQUEST, message);
    }

    /**
     * 请求失败
     *
     * @param data 失败数据
     * @return
     */
    public static JsonResult failure(Object data) {
        return new JsonResult(Status.FAILURE, data);
    }

    /**
     * 请求错误
     *
     * @return
     */
    public static JsonResult error() {
        return new JsonResult(Status.SERVER_ERROR);
    }

    /**
     * 请求错误
     *
     * @param message 错误信息
     * @return
     */
    public static JsonResult error(String message) {
        return new JsonResult(Status.SERVER_ERROR, message);
    }

    /**
     * 请求重定向
     *
     * @param redirectUrl 重定向地址
     * @return
     */
    public static JsonResult redirect(String redirectUrl) {
        return new JsonResult(Status.REDIRECT, null, redirectUrl);
    }

    public JsonResult() {
        this(Status.SUCCESS);
    }

    public JsonResult(boolean success) {
        this(success ? Status.SUCCESS : Status.FAILURE);
    }

    public JsonResult(boolean success, String message) {
        this(success ? Status.SUCCESS : Status.FAILURE, message);
    }

    public JsonResult(Object data) {
        this(Status.SUCCESS, data);
    }

    public JsonResult(boolean success, Object data) {
        this(success ? Status.SUCCESS : Status.FAILURE, null, data);
    }

    public JsonResult(Status status) {
        this(status, null, null);
    }

    public JsonResult(Status status, String message) {
        this(status, message, null);
    }

    public JsonResult(Status status, Object data) {
        this(status, null, data);
    }

    public JsonResult(Status status, String message, Object data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return StringUtils.isEmpty(message) ? EnumDisplayUtils.getDisplayText(status.name(), Status.class) : message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return EnumDisplayUtils.getDisplayOrder(status);
    }

}
