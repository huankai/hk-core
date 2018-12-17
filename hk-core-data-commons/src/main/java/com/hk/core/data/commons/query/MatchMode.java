package com.hk.core.data.commons.query;


/**
 * Sql 查询匹配模式
 *
 * @author kevin
 */
public enum MatchMode {

    EXACT {
        public String toMatchString(String pattern) {
            return pattern;
        }
    },

    START {
        public String toMatchString(String pattern) {
            return String.format("%s%%", pattern);
        }
    },

    END {
        public String toMatchString(String pattern) {
            return String.format("%%%s", pattern);
        }
    },

    ANYWHERE {
        public String toMatchString(String pattern) {
            return String.format("%%%s%%", pattern);
        }
    };

    public abstract String toMatchString(String pattern);

}
