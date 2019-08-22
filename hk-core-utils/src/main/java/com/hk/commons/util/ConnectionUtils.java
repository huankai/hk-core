package com.hk.commons.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kevin
 * @date 2019-8-21 11:04
 */
public abstract class ConnectionUtils {

    public static void close(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        closeResultSet(resultSet);
        closePreparedStatement(statement);
        closeConnection(connection);
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (null != preparedStatement) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    public static void closeConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
