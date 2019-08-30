package com.hk.core.data.jpa.repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kevin
 * @date 2019-8-30 16:49
 */
class HqlUtils {

    private static final Pattern compile = Pattern.compile("\\?");

    /**
     * jpa 参数需要以 ?1 ?2 这样的占位符，而使用 Jdbc 用的是 ? ? ?，这里将 ? ? ? 替换为 ?1 ?2 ?3 ...
     *
     * @param hql hql
     * @return hql
     */
    static String replaceHqlParameter(String hql) {
        StringBuffer sb = new StringBuffer();
        Matcher m = compile.matcher(hql);
        int index = 0;
        while (m.find()) {
            String param = m.group();
            m.appendReplacement(sb, param + (index++));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
