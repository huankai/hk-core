package com.hk.commons.converters;

import com.hk.commons.util.JsonUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * json string to  Map
 *
 * @author kevin
 * @date 2019-7-8 9:30
 */
public class JsonStringToMapConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Map.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        return JsonUtils.deserializeMap(source.toString(), targetType.getMapKeyTypeDescriptor().getType(),
                targetType.getMapValueTypeDescriptor().getType());
    }
}
