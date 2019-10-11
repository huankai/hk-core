package com.hk.core.jdbc.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.JsonUtils;
import com.hk.core.jdbc.query.ConditionQueryModel;
import com.hk.core.jdbc.query.DateRangeCondition;
import com.hk.core.jdbc.query.RangeCondition;
import com.hk.core.jdbc.query.SimpleCondition;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;

import java.io.IOException;
import java.util.List;

/**
 * 前端传过来的json 解析， json 格式如下:
 * <pre>
 * {
 * 	"pageIndex":11,
 * 	"pageSize":10,
 * 	"orders":[
 *        {
 * 	        "field": "",
 * 	        "desc": false
 *        }
 * 	]
 * 	"param":{
 * 		"simple":[
 *          {
 *              "field": "cityType",
 * 				"operator":"EQ",
 * 				"value": 1
 *          }
 * 		],
 * 		"dateRange":[
 *          {
 * 			    "range":"TD",
 * 				"field":"createDate"
 *          }
 * 		]
 *    }
 * }
 * </pre>
 *
 * @author kevin
 * @date 2019-8-30 12:48
 */
public final class ConditionQueryModelDeserializer extends JsonDeserializer<ConditionQueryModel> {

    @Override
    public ConditionQueryModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ConditionQueryModel queryModel = new ConditionQueryModel();
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        fullPageProperty(queryModel, node);
        fullOrderProperty(queryModel, node);
        fullParamProperty(queryModel, node);
        return queryModel;
    }

    private void fullParamProperty(ConditionQueryModel queryModel, JsonNode node) {
        JsonNode paramNode = node.get("param");
        if (null != paramNode) {
            JsonNode simpleConditionNode = paramNode.get("simple");
            if (null != simpleConditionNode) {
                List<SimpleCondition> simpleConditions = JsonUtils.deserializeList(simpleConditionNode.toString(),
                        SimpleCondition.class);
                if (CollectionUtils.isNotEmpty(simpleConditions)) {
                    queryModel.getParam().addConditions(simpleConditions);
                }
            }
            JsonNode dateRangeConditionNode = paramNode.get("dateRange");
            if (null != dateRangeConditionNode) {
                List<DateRangeCondition> dateRangeConditions = JsonUtils.deserializeList(dateRangeConditionNode.toString(),
                        DateRangeCondition.class);
                if (CollectionUtils.isNotEmpty(dateRangeConditions)) {
                    queryModel.getParam().addConditions(dateRangeConditions);
                }
            }

            JsonNode rangeNode = paramNode.get("range");
            if (null != rangeNode) {
                List<RangeCondition<?>> rangeConditions = JsonUtils.deserialize(rangeNode.toString(), List.class, RangeCondition.class, Object.class);
                if (CollectionUtils.isNotEmpty(rangeConditions)) {
                    queryModel.getParam().addConditions(rangeConditions);
                }
            }
        }
    }

    private void fullOrderProperty(ConditionQueryModel queryModel, JsonNode node) {
        JsonNode orderNode = node.get("orders");
        if (null != orderNode) {
            List<Order> orders = JsonUtils.deserializeList(orderNode.toString(), Order.class);
            if (CollectionUtils.isNotEmpty(orders)) {
                queryModel.setOrders(orders);
            }
        }
    }

    private void fullPageProperty(ConditionQueryModel queryModel, JsonNode node) {
        JsonNode pageIndexNode = node.get("pageIndex");
        if (null != pageIndexNode) {
            queryModel.setPageIndex(pageIndexNode.asInt(0));
        }
        JsonNode pageSizeNode = node.get("pageSize");
        if (null != pageSizeNode) {
            queryModel.setPageSize(pageSizeNode.asInt(QueryModel.DEFAULT_PAGE_SIZE));
        }
    }
}
