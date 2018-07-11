package com.hk.core.repository;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;


/**
 * @param <T>
 * @param <PK>
 * @author: kevin
 */
public interface BaseRepository<T extends Persistable<PK>, PK extends Serializable> {

}
