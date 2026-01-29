package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getMySQLConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    private static Connection getPostgresConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "app", "pass");
    }

    private static Connection getConnection() throws SQLException {
        String db = System.getProperty("db");

        if ("postgres".equals(db)) {
            return getPostgresConnection();
        }
        return getMySQLConnection();
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        try (var connection = getConnection()) {
            var sql = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
            return runner.query(connection, sql, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var sql = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConnection()) {
            return runner.query(connection, sql, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConnection();
        runner.update(connection, "DELETE FROM credit_request_entity");
        runner.update(connection, "DELETE FROM payment_entity");
        runner.update(connection, "DELETE FROM order_entity");
    }
}
