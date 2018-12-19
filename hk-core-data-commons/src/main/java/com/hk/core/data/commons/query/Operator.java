package com.hk.core.data.commons.query;


/**
 * @author kevin
 */
public enum Operator {

    /**
     * example:
     * <pre>
     * WHERE column = value
     *
     * </pre>
     */
    EQ,

    /**
     * example:
     * <pre>
     * WHERE column <> value
     *
     * </pre>
     */
    NE,

    /**
     * example:
     * <pre>
     * WHERE column LIKE value
     *
     * </pre>
     */
    LIKE,

    /**
     * example:
     * <pre>
     * WHERE column LIKE value%
     *
     * </pre>
     */
    LIKESTART,

    /**
     * example:
     * <pre>
     * WHERE column LIKE %value
     *
     * </pre>
     */
    LIKEEND,

    /**
     * example:
     * <pre>
     * WHERE column LIKE %value%
     *
     * </pre>
     */
    LIKEANYWHERE,

    /**
     * example:
     * <pre>
     * WHERE column > value
     *
     * </pre>
     */
    GT,

    /**
     * example:
     * <pre>
     * WHERE column < value
     *
     * </pre>
     */
    LT,

    /**
     * example:
     * <pre>
     * WHERE column >= value
     *
     * </pre>
     */
    GTE,

    /**
     * example:
     * <pre>
     * WHERE column <= value
     *
     * </pre>
     */
    LTE,

    /**
     * example:
     * <pre>
     * WHERE column IN( value1,value2,...)
     *
     * </pre>
     */
    IN,

    /**
     * example:
     * <pre>
     * WHERE column NOT IN( value1,value2,...)
     *
     * </pre>
     */
    NOTIN,

    /**
     * example:
     * <pre>
     * WHERE column BETWEEN value1 AND value2
     *
     * </pre>
     */
    BETWEEN,

    /**
     * example:
     * <pre>
     * WHERE column IS NULL
     *
     * </pre>
     */
    ISNULL,

    /**
     * example:
     * <pre>
     * WHERE column IS NOT NULL
     *
     * </pre>
     */
    ISNOTNULL

}
