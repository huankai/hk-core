package com.hk.commons.validator;

import java.util.List;

/**
 * @author kevin
 * @date 2018-08-31 14:10
 */
public interface DictService {

    List<Byte> getDictValueListByCodeId(Long codeId);

    String getCodeName(Long baseCodeId, Number value);
}
