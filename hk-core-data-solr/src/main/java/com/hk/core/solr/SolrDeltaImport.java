package com.hk.core.solr;

import java.lang.annotation.*;

/**
 * Solr Delta Import
 *
 * @author: kevin
 * @date: 2018-07-04 12:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SolrDeltaImport {

    /**
     * entities
     *
     * @return
     */
    String[] entities();
}
