package com.hk.core.web;

import com.hk.commons.fastjson.JsonUtils;
import com.hk.commons.http.get.SimpleGetHttpExecutor;
import com.hk.commons.util.SpringContextHolder;

import java.io.IOException;

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
        try {
            result = httpExecutor.execute("http://127.0.0.1:8002/api/apps/" + appCode, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtils.parseObject(result,JsonResult.class).getData().toString();
    }
}
