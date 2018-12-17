package com.hk.core.data.commons.utils;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.StringUtils;

/**
 * @author kevin
 * @date 2018-09-19 23:26
 */
abstract class SqlEscapeUtils {

    private static final String[] SQL_KEY_WORD = {"CREATE", "AND", "OR", "USE", "--", ";",
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "GRANT", "EXECUTE", "EXEC"};

    static String escape(String args) {
        return (StringUtils.isEmpty(args) || ArrayUtils.contains(SQL_KEY_WORD, args.trim().toUpperCase()))
                ? null : args;
    }
}
