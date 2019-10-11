package com.hk.commons.configstore;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 配置属性存储
 *
 * @author kevin
 * @date 2019-8-20 17:40
 */
public interface ConfigStorage<T extends ConfigID> {

    List<T> getAll();

    Optional<T> findById(Serializable id);
}
