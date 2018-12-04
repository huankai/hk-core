package com.hk.core.solr.query;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.data.commons.query.AndOr;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-12-03 17:25
 */
public class CompositeCondition implements Condition {

    @Getter
    @Setter
    private AndOr andOr;

    @Getter
    @Setter
    private List<Condition> conditions;


    public CompositeCondition() {
        this.andOr = AndOr.AND;
    }

    /**
     * @param andOr      andOr
     * @param conditions conditions
     */
    public CompositeCondition(AndOr andOr, List<Condition> conditions) {
        this.andOr = andOr;
        this.conditions = conditions;
    }

    /**
     * 添加条件
     *
     * @param condition condition
     */
    public CompositeCondition addCondition(Condition condition) {
        conditions.add(condition);
        return this;
    }

    /**
     * 添加条件
     *
     * @param conditions conditions
     */
    public CompositeCondition addConditions(Condition... conditions) {
        CollectionUtils.addAllNotNull(this.conditions, conditions);
        return this;
    }

    /**
     * 删除指定条件
     *
     * @param condition condition
     */
    public CompositeCondition removeCondition(Condition condition) {
        conditions.remove(condition);
        return this;
    }

    /**
     * 删除指定条件
     *
     * @param index index
     */
    public CompositeCondition removeCondition(int index) {
        conditions.remove(index);
        return this;
    }

    /**
     * 清除所有条件
     */
    public void clearConditions() {
        conditions.clear();
    }

    @Override
    public Criteria toSolrCriteria() {
        SimpleQuery query = new SimpleQuery();
        query.setDefaultOperator(andOr == AndOr.AND ? Query.Operator.AND : Query.Operator.OR);
        if (CollectionUtils.isNotEmpty(conditions)) {
            for (Condition condition : conditions) {
                Criteria criteria = condition.toSolrCriteria();
                if (null != criteria) {
                    query.addCriteria(criteria);
                }
            }
        }
        return query.getCriteria();
    }
}
