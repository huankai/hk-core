package com.hk.core.web;

import com.google.common.collect.Maps;
import com.hk.commons.fastjson.JsonUtils;
import com.hk.commons.http.get.SimpleGetHttpExecutor;
import com.hk.commons.util.SpringContextHolder;
import com.hk.commons.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author: huangkai
 * @date 2018-04-17 11:35
 */
public abstract class AppCodeUtils {

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getAppCode() {
        return SpringContextHolder.getApplicationContext().getEnvironment().getRequiredProperty("spring.application.name");
    }

    public static String getCurrentAppId() {
        String appCode = getAppCode();
        SimpleGetHttpExecutor httpExecutor = new SimpleGetHttpExecutor();
        String result = null;
        Map<String, Object> params = Maps.newHashMap();
        HttpServletRequest request = Webs.getHttpServletRequest();
        String lang = request.getParameter("lang");
        if (StringUtils.isNotEmpty(lang)) {
            params.put("lang", lang);
        }
        try {
            result = httpExecutor.execute("http://127.0.0.1:8002/api/apps/" + appCode, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtils.parseObject(result, JsonResult.class).getData().toString();
    }
}
