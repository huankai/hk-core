package com.hk.commons.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * 商品 sku 算法，根据商品属性生成 sku 组合参数
 * </pre>
 *
 * @author kevin
 * @date 2019-7-8 17:39
 */
public abstract class SkuUtils {

    /**
     * <pre>
     *
     * 使用 string 字符串生成 sku 结果
     *  example:
     *    text : 颜色:蓝色男款,白色男款;尺码:38,39
     *  return:
     *      [
     *          [{"text":"颜色","value":"蓝色男款"},{"text":"尺码","value":"38"}],
     *          [{"text":"颜色","value":"蓝色男款"},{"text":"尺码","value":"39"}],
     *          [{"text":"颜色","value":"白色男款"},{"text":"尺码","value":"38"}],
     *          [{"text":"颜色","value":"白色男款"},{"text":"尺码","value":"39"}]
     *      ]
     * </pre>
     *
     * @param text sku 属性参数，多个属性必须以英文 ;　分隔，每个属性多个值必须以英文 , 分隔。
     * @return
     */
    public static List<List<TextValueItem>> getSkuAttributeList(String text) {
        String[] nameValues = StringUtils.tokenizeToStringArray(text, ";");
        List<NameValues> lists = new ArrayList<>(nameValues.length);
        int excludeFirstValuesSize = 1;//排除第一个元素后其它总元素总数
        for (int index = 0; index < nameValues.length; index++) {
            String nameValue = nameValues[index];
            String[] keyValue = StringUtils.tokenizeToStringArray(nameValue, ":");
            if (keyValue.length == 2) { //以 : 分隔的长度必须为2 ,key:value1,value2...
                String name = keyValue[0];
                String[] values = StringUtils.splitByComma(keyValue[1]);
                if (ArrayUtils.isNotEmpty(values)) {
                    if (index != 0) {
                        excludeFirstValuesSize = excludeFirstValuesSize * values.length;
                    }
                    lists.add(new NameValues(name, ArrayUtils.asArrayList(values)));
                }
            }
        }
        return getSkuAttributeList(lists);
    }

    /**
     * @param nameValues nameValues
     * @return
     */
    public static List<List<TextValueItem>> getSkuAttributeList(List<NameValues> nameValues) {
        if (CollectionUtils.isNotEmpty(nameValues)) {
            List<Position> positions = new ArrayList<>();
            int excludeFirstValuesSize = 1;
            for (int index = 0, size = nameValues.size(); index < size; index++) {
                NameValues item = nameValues.get(index);
                if (CollectionUtils.isNotEmpty(item.values)) {
                    if (index != 0) { // 第一个元素不放进去
                        excludeFirstValuesSize = excludeFirstValuesSize * item.values.size();
                        positions.add(new Position(index, item.values.size()));
                    }
                }
            }
            NameValues firstNameValue = nameValues.get(0);
            List<List<TextValueItem>> result = new ArrayList<>();
            Collections.reverse(positions);
            Collections.reverse(nameValues);
            for (String firstValue : firstNameValue.values) {
                for (int i = 0; i < excludeFirstValuesSize; i++) {
                    List<TextValueItem> list = new ArrayList<>();
                    list.add(new TextValueItem(firstNameValue.name, firstValue));
                    if (CollectionUtils.isNotEmpty(positions)) {
                        CollectionUtils.addAll(list, getList(i, positions, nameValues));
                    }
                    result.add(list);
//                    System.out.println(JsonUtils.serialize(list));
                }
            }
            return result;

        }
        return Collections.emptyList();
    }

    /**
     * sku 核心算法
     *
     * @param index      sku 索引，倒叙排序
     * @param positions  记录每个sku参数在集合中的位置和 sku 名称的参数值个数
     * @param nameValues nameValues
     */
    private static List<TextValueItem> getList(int index, List<Position> positions, List<NameValues> nameValues) {
        List<TextValueItem> result = new ArrayList<>(positions.size());
        int before = index % positions.get(0).getMaxPosition();
        for (int i = 0, size = positions.size(); i < size; i++) {
            NameValues nameValue = nameValues.get(i);
            if (before >= nameValue.values.size()) {
                before = before % nameValue.values.size();
            }
            result.add(new TextValueItem(nameValue.name, nameValue.values.get(before)));
            before = index / positions.stream().limit(i + 1).map(Position::getMaxPosition).reduce(1, (x, y) -> x * y);
        }
        Collections.reverse(result);
        return result;
    }

    @Data
    @AllArgsConstructor
    private static class Position {

        /**
         * sku 在集合中的位置
         */
        private int index;

        /**
         * sku 在集合中的最大个数
         */
        private int maxPosition;
    }

    @Data
    @AllArgsConstructor
    public static class NameValues {

        /**
         * sku 参数名
         */
        private String name;

        /**
         * sku 参数值
         */
        private List<String> values;

    }
}
