package com.hk.core.data.commons.utils;

import com.hk.commons.util.ArrayUtils;

/**
 * @author: kevin
 * @date: 2018-09-19 23:26
 */
public abstract class SqlEscapeUtils {

    private static final String[] SQL_KEY_WORD = {"CREATE", "AND", "OR", "USE", "--", ";",
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "GRANT", "EXECUTE", "EXEC"};

    public static String escape(String args) {
        return ArrayUtils.contains(SQL_KEY_WORD, args.trim().toUpperCase()) ? null : args;
    }
}
