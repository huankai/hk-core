package com.hk.commons.validator;

import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.SpringContextHolder;
import com.hk.commons.validator.constraints.EnumDict;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author kevin
 * @date 2018-08-31 10:57
 */
public class EnumDictValidator implements ConstraintValidator<EnumDict, Byte> {

    private boolean notNull;

    private long codeId;

    private DictService dictService;

    @Override
    public void initialize(EnumDict constraintAnnotation) {
        this.notNull = constraintAnnotation.notNull();
        this.codeId = constraintAnnotation.codeId();
    }

    @Override
    public boolean isValid(Byte value, ConstraintValidatorContext context) {
        if (null == value) {
            return !notNull;
        }
        List<Byte> list = getDictService().getDictValueListByCodeId(codeId);
        return CollectionUtils.contains(list, value);
    }

    public DictService getDictService() {
        if (null == dictService) {
            dictService = SpringContextHolder.getBean(DictService.class);
        }
        return dictService;
    }
}
