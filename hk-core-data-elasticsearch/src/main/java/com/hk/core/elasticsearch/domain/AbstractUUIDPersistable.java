package com.hk.core.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

/**
 * @author huangkai
 * @date 2019-04-14 13:17
 */
public abstract class AbstractUUIDPersistable implements Persistable<String> {

    @Id
    @Getter
    @Setter
    private String id;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return StringUtils.isEmpty(getId());
    }
}
