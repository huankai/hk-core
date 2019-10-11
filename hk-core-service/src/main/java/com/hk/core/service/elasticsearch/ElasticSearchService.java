package com.hk.core.service.elasticsearch;

import com.hk.commons.util.StringUtils;
import com.hk.core.elasticsearch.analyzer.IKanalyzer;
import com.hk.core.elasticsearch.query.Condition;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.BaseService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kevin
 * @date 2019-6-28 11:51
 */
public interface ElasticSearchService<T extends Persistable<String>> extends BaseService<T, String> {

    /**
     * 分页查询
     *
     * @param conditions 查询条件
     * @param pageIndex  分页参数，从第几页开始
     * @param pageSize   分页参数，查询多少条记录
     * @param orders     查询排序
     * @return {@link QueryPage}
     */
    QueryPage<T> findByPage(List<Condition> conditions, int pageIndex, int pageSize, Order... orders);

    /**
     * 查询
     *
     * @param conditions 查询条件
     * @param orders     查询排序
     * @return {@link List}
     */
    List<T> findAll(List<Condition> conditions, Order... orders);

    /**
     * 建议词查询
     *
     * @param suggestBuilder suggestBuilder
     * @return {@link SearchResponse}
     */
    SearchResponse suggest(SuggestBuilder suggestBuilder);

    /**
     * 建议词查询
     *
     * @param suggestField suggest 字段名称，该字段类型必须为: completion
     *                     {@link org.springframework.data.elasticsearch.annotations.CompletionField},
     *                     {@link org.springframework.data.elasticsearch.core.completion.Completion}
     * @param keyword      查询关键字
     * @return 建议词
     */
    default List<String> suggest(String suggestField, String keyword) {
        if (StringUtils.isBlank(keyword)) { // 如果 keywork 值为 null 时查询会报错!
            return Collections.emptyList();
        }
        String suggestName = getClass().getSimpleName();
        CompletionSuggestionBuilder builder = SuggestBuilders.completionSuggestion(suggestField)
                .skipDuplicates(true)
                .text(keyword)
                .analyzer(IKanalyzer.IK_MAX_WORD_ANALYZER)
                .size(10);
        SearchResponse searchResponse = suggest(new SuggestBuilder().addSuggestion(suggestName, builder));
        List<? extends Suggest.Suggestion.Entry.Option> options = searchResponse.getSuggest()
                .getSuggestion(suggestName)
                .getEntries()
                .get(0)
                .getOptions();
        List<String> result = new ArrayList<>(options.size());
        for (Suggest.Suggestion.Entry.Option option : options) {
            result.add(option.getText().string());
        }
        return result;
    }
}
