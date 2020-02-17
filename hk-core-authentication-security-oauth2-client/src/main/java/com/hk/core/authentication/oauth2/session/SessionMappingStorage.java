package com.hk.core.authentication.oauth2.session;

import javax.servlet.http.HttpSession;

/**
 * 客户端 Session 存储，使用单点退出后，session 将会被销毁
 *
 * @author kevin
 * @date 2019-5-18 10:06
 */
@Deprecated
public interface SessionMappingStorage {

    /**
     * 根据 mappingId 删除 HttpSession
     *
     * @param mappingId mappingId
     * @return {@link HttpSession}
     */
    HttpSession removeSessionByMappingId(String mappingId);

    /**
     * 根据 SessionId 删除 HttpSession
     *
     * @param sessionId sessionId
     */
    void removeBySessionById(String sessionId);

    /**
     * 添加 HttpSession
     *
     * @param mappingId mappingId
     * @param session   {@link HttpSession}
     */
    void addSessionById(String mappingId, HttpSession session);
}
