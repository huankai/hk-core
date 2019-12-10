package com.hk.commons.poi.excel.read.handler;

import com.hk.commons.poi.ReadException;
import com.hk.commons.poi.excel.exception.ExcelReadException;
import com.hk.commons.poi.excel.model.*;
import com.hk.commons.poi.excel.read.interceptor.ValidationInterceptor;
import com.hk.commons.poi.excel.read.validation.Validationable;
import com.hk.commons.poi.excel.util.ReadExcelUtils;
import com.hk.commons.poi.excel.util.WriteExcelUtils;
import com.hk.commons.util.ClassUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.commons.util.StringUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author kevin
 */
public abstract class AbstractReadHandler<T> {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 解析的一些参数
     */

    protected final ReadParam<T> readParam;

    /**
     * 标题信息，解析标题的时候才会有值
     */
    protected List<Title> titles;

    protected AbstractReadHandler(ReadParam<T> readParam) {
        this.readParam = readParam;
    }

    /**
     * 获取指定列的标题信息
     *
     * @param columnIndex columnIndex
     * @return column mapping title
     */
    protected final Title getTitle(int columnIndex) {
        return titles.stream().filter(item -> item.getColumn() == columnIndex).findFirst()
                .orElseThrow(() -> new ExcelReadException("第[" + columnIndex + "]列 Title 不存在"));
    }

    /**
     * 获取标题值
     *
     * @param columnIndex columnIndex
     * @return column mapping title value
     */
    protected final String getTitleValue(int columnIndex) {
        return getTitle(columnIndex).getValue();
    }

    /**
     * 获取列对应的属性名
     *
     * @param columnIndex columnIndex
     * @return column mapping title propertyName
     */
    protected final String getPropertyName(int columnIndex) {
        return getTitle(columnIndex).getPropertyName();
    }

    /**
     * 设置解析的标题
     *
     * @param titles setTitles
     */
    protected void setTitles(List<Title> titles) {
        this.titles = titles;
    }

    /**
     * 是否为标题行
     *
     * @param rowNum
     * @return if (rowNum == titleRow),return to true;otherwise, return to false
     */
    protected final boolean isTitleRow(int rowNum) {
        return rowNum == readParam.getTitleRow();
    }

    /**
     * 在没有解析标题行时，为-1
     *
     * @return 返回最大解析的列
     */
    protected final int getMaxColumnIndex() {
        return null == titles ? -1 : titles.size();
    }

    /**
     * 设置属性
     *
     * @param wrapper     wrapper对象
     * @param columnIndex 属性所在列
     * @param value       要设置的属性值
     */
    protected void setWrapperBeanValue(BeanWrapper wrapper, int columnIndex, Object value)
            throws BeansException {
        if (Objects.nonNull(value)) {
            var propertyName = getPropertyName(columnIndex);
            if (StringUtils.isNotEmpty(propertyName)) {
                value = trimToValue(value);
                var isNestedProperty = StringUtils.indexOf(propertyName, WriteExcelUtils.NESTED_PROPERTY) != -1;
                if (isNestedProperty) {
                    var nestedPropertyPrefix = StringUtils.substringBefore(propertyName, WriteExcelUtils.NESTED_PROPERTY);
                    Class<?> propertyType = wrapper.getPropertyType(nestedPropertyPrefix);
                    propertyName = String.format(propertyName, 0);
                    var propertyValue = wrapper.getPropertyValue(propertyName);
                    if (null == propertyValue) {
                        if (!ClassUtils.isAssignable(Collection.class, propertyType)) {
                            throw new BeanCreationException("NestedProperty 类型必须为 Collection");
                        }
                        propertyValue = newNestedPropertyValue(propertyType);
                    }
                    var size = getNestedPropertySize(propertyValue);
                    if (isNewObject(wrapper.getWrappedClass(), nestedPropertyPrefix, columnIndex)) {
                        size = size + 1;
                    }
                    propertyName = String.format(propertyName, size);
                }
                wrapper.setPropertyValue(propertyName, value);
            }
        }
    }

    private boolean isNewObject(Class<?> propertyType, String nestedPropertyPrefix, int currentColumnIndex) {
        try {
            var type = (ParameterizedType) propertyType.getDeclaredField(nestedPropertyPrefix).getGenericType();
            return ReadExcelUtils.getMinColumnWithExcelCellAnnotations((Class<?>)type.getActualTypeArguments()[0]) == currentColumnIndex;
        } catch (NoSuchFieldException e) {
            throw new BeanCreationException(e.getMessage());
        }
    }

    /**
     * 获取当前Nested 属性的size
     *
     * @param value value
     * @return
     */
    private int getNestedPropertySize(Object value) {
        return (int) CollectionUtils.size((Iterable<?>) value);
    }

    /**
     * 创建 Nested属性对象，目前只支持 {@link List} ,{@link Set} ,{@link Queue} ,{@link Collection} 类型
     *
     * @param propertyType propertyType
     */
    private Object newNestedPropertyValue(Class<?> propertyType) {
        if (ClassUtils.isAssignable(List.class, propertyType)) {
            return new ArrayList<>(0);
        }
        if (ClassUtils.isAssignable(Set.class, propertyType)) {
            return new HashSet<>(0);
        }
        if (ClassUtils.isAssignable(Queue.class, propertyType)) {
            return new LinkedList<>();
        }
        if (ClassUtils.isAssignable(Collection.class, propertyType)) {
            return new ArrayList<>(0);
        }
        throw new BeanCreationException("NestedProperty 类型必须为 Collection");
    }

    /**
     * <pre>
     * 忽略首、末有空字符号串或者有 \n 字符串
     * 如果参数中有设置 trim == true 时
     * "a" ---------> "a"
     * "  a" ---------> "a"
     * "a  " ---------> "a"
     * 如果参数中有设置 ingoreLineBreak == true 时
     * "a\n" ---------> "a"
     * "a\nb" ---------> "ab"
     * </pre>
     *
     * @param value value
     * @return trim value
     */
    private Object trimToValue(Object value) {
        var result = ObjectUtils.toString(value);
        if (readParam.isTrim()) {
            result = StringUtils.trimToNull(result);
        }
        if (StringUtils.isNotEmpty(result) && readParam.isIgnoreLineBreak()) {
            result = StringUtils.replace(result, StringUtils.LF, StringUtils.EMPTY);
        }
        return result;
    }

    /**
     * 数据有效性验证
     *
     * @param result result
     */
    protected void validate(ReadResult<T> result) {
        List<Validationable<T>> validations = readParam.getValidationList();
        if (CollectionUtils.isNotEmpty(validations)) {
            final ValidationInterceptor<T> interceptor = readParam.getInterceptor();
            interceptor.preValidate(result);
            List<SheetData<T>> sheetDataList = result.getSheetDataList();
            var size = CollectionUtils.size(sheetDataList);
            if (size > 1) {
                CountDownLatch countDownLatch = new CountDownLatch((int) size);
                for (SheetData<T> sheetData : sheetDataList) {
                    new SheetValidateThread(countDownLatch, sheetData, result).start();
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new ReadException("ThreadName: " + Thread.currentThread().getName() + ",,Message:" + e.getMessage(), e);
                }
            } else if (size == 1) {
                doValidate(sheetDataList.get(0), result);
            }
            interceptor.afterComplete(result);
        }
    }

    private void doValidate(SheetData<T> dataSheet, ReadResult<T> result) {
        ValidationInterceptor<T> interceptor = readParam.getInterceptor();
        var rowIndex = readParam.getDataStartRow();
        ListIterator<T> listIterator = dataSheet.getData().listIterator();
        while (listIterator.hasNext()) {
            var t = listIterator.next();
            List<InvalidCell> invalidCells = new ArrayList<>();
            if (interceptor.beforeValidate(t)) {
                for (Validationable<T> validation : readParam.getValidationList()) {
                    List<InvalidCell> errors = validation.validate(t, rowIndex, titles);
                    if (CollectionUtils.isNotEmpty(errors)) {
                        invalidCells.addAll(errors);
                        if (!validation.errorNext()) {
                            break;
                        }
                    }
                }
                interceptor.afterValidate(t);
            }
            if (!invalidCells.isEmpty()) {
                result.addErrorLog(new ErrorLog<>(dataSheet.getSheetName(), rowIndex, t, invalidCells));
                listIterator.remove();
            }
            rowIndex++;
        }
    }

    @AllArgsConstructor
    private class SheetValidateThread extends Thread {

        private final CountDownLatch countDownLatch;

        private SheetData<T> sheetData;

        private ReadResult<T> result;

        @Override
        public void run() {
            try {
                doValidate(sheetData, result);
            } finally {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        }
    }

}
