package com.hk.commons.configstore;

import com.hk.commons.util.ObjectUtils;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 使用 yml | properties 存储
 *
 * @author kevin
 * @date 2019-8-20 17:42
 */
@RequiredArgsConstructor
public class PropertiesConfigStorage<T extends ID> implements ConfigStorage<T> {

    private final List<T> data;

    @Override
    public List<T> getAll() {
        return data;
    }

    @Override
    public Optional<T> findById(Serializable id) {
        return data.stream().filter(item -> ObjectUtils.nullSafeEquals(item.getId(), id)).findFirst();
    }
}
