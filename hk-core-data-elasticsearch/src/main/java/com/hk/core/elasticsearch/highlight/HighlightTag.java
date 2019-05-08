package com.hk.core.elasticsearch.highlight;

/**
 * 高亮查询
 *
 * @author huangkai
 * @date 2019-05-07 22:09
 */
public interface HighlightTag {

    /**
     * 高亮前缀标签
     */
    String PRE_TAG = "<font color='red'>";

    /**
     * 高亮后缀标签
     */
    String POST_TAG = "</font>";
}
