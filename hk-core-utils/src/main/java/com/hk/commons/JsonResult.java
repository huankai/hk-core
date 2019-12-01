package com.hk.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.SpringContextHolder;
import com.hk.commons.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Json返回结果
 *
 * @author kevin
 * @date 2017年9月27日上午11:09:08
 */
@SuppressWarnings("serial")
@AllArgsConstructor
public final class JsonResult<T> implements Serializable {

    /**
     * 返回状态
     */
    @Getter
    @Setter
    private int statusCode;

    /**
     * 返回消息信息
     */
    @Setter
    private String message;
    /**
     * 返回数据
     */
    @Getter
    @Setter
    private T data;


    /**
     * 请求成功
     *
     * @return JsonResult
     */
    public static JsonResult<Void> success() {
        return success(null);
    }

    public static JsonResult<Void> success(String message) {
        return new JsonResult<>(true, message);
    }

    /**
     * 请求成功
     *
     * @return JsonResult
     */
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(data);
    }

    /**
     * 请求失败
     *
     * @return JsonResult
     */
    public static JsonResult<Void> failure() {
        return new JsonResult<>(Status.FAILURE);
    }

    /**
     * 请求失败
     *
     * @param message 失败信息
     * @return JsonResult
     */
    public static JsonResult<Void> failure(String message) {
        return new JsonResult<>(false, message);
    }

    /**
     * 请求姿势不正确
     *
     * @param message message
     * @return JsonResult
     */
    public static JsonResult<Void> badRequest(String message) {
        return new JsonResult<>(Status.BAD_REQUEST, message);
    }

    /**
     * 用户未认证
     *
     * @param message message
     * @return JsonResult
     */
    public static JsonResult<Void> unauthorized(String message) {
        return new JsonResult<>(Status.UNAUTHORIZED, message);
    }

    /**
     * 访问拒绝
     *
     * @param message message
     * @return JsonResult
     */
    public static JsonResult<Void> forbidden(String message) {
        return new JsonResult<>(Status.FORBIDDEN, message);
    }

    /**
     * 请求失败
     *
     * @param data 失败数据
     * @return JsonResult
     */
    public static <T> JsonResult<T> failure(T data) {
        return new JsonResult<>(Status.FAILURE, data);
    }

    /**
     * 请求错误
     *
     * @return JsonResult
     */
    public static JsonResult<Void> error() {
        return new JsonResult<>(Status.SERVER_ERROR);
    }

    /**
     * 请求错误
     *
     * @param message 错误信息
     * @return JsonResult
     */
    public static JsonResult<Void> error(String message) {
        return new JsonResult<>(Status.SERVER_ERROR, message);
    }

    /**
     * 请求重定向
     *
     * @param redirectUrl 重定向地址
     * @return JsonResult
     */
    public static JsonResult<String> redirect(String redirectUrl) {
        return new JsonResult<>(Status.REDIRECT, null, redirectUrl);
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

    public JsonResult(T data) {
        this(Status.SUCCESS, data);
    }

    public JsonResult(boolean success, T data) {
        this(success ? Status.SUCCESS : Status.FAILURE, null, data);
    }

    public JsonResult(Status status) {
        this(status, null, null);
    }

    public JsonResult(Status status, String message) {
        this(status, message, null);
    }

    public JsonResult(Status status, T data) {
        this(status, null, data);
    }

    public JsonResult(Status status, String message, T data) {
        this.statusCode = Status.getOrder(status);
        this.data = data;
        this.message = SpringContextHolder.getMessageWithDefault(message, message);
    }

    public String getMessage() {
        if (StringUtils.isEmpty(this.message)) {
            String message = Status.getMessage(statusCode);
            return SpringContextHolder.getMessageWithDefault(message, message);
        }
        return this.message;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.statusCode == Status.SUCCESS_STATUS;
    }

    public <E> JsonResult<E> of(E data) {
        return new JsonResult<>(statusCode, message, data);
    }

}
