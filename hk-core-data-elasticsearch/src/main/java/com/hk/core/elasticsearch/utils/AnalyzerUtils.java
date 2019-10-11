package com.hk.core.elasticsearch.utils;

import com.hk.commons.util.Lazy;
import com.hk.commons.util.SpringContextHolder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.client.AdminClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 分词
 *
 * @author kevin
 * @date 2019-6-28 9:09
 */
public abstract class AnalyzerUtils {

    private static final Lazy<AdminClient> ADMIN_CLIENT = Lazy.of(() -> SpringContextHolder.getBean(AdminClient.class));

    /**
     * @param analyzer 分词器
     * @param text     分词文本
     * @return 分词结果
     */
    public static String[] analyzer(String analyzer, String text) {
        List<AnalyzeResponse.AnalyzeToken> tokens = ADMIN_CLIENT.get()
                .indices()
                .analyze(new AnalyzeRequest().analyzer(analyzer).text(text))
                .actionGet()
                .getTokens();
        List<String> result = new ArrayList<>(tokens.size());
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            result.add(token.getTerm());
        }
        return result.toArray(new String[0]);
    }

}
