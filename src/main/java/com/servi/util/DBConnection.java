package com.servi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_HOST     = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT     = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DB_NAME     = System.getenv().getOrDefault("DB_NAME", "servi");
    private static final String USER        = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD    = System.getenv().getOrDefault("DB_PASSWORD", "");
    private static final String URL         = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("DBConnection: driver MySQL cargado OK");
        } catch (ClassNotFoundException e) {
            System.err.println("DBConnection ERROR: driver no encontrado — " + e.getMessage());
            throw new RuntimeException("No se encontró el driver MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("DBConnection: intentando conexión a " + URL);
        try {
            Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DBConnection: conexión OK");
            return c;
        } catch (SQLException e) {
            System.err.println("DBConnection ERROR getConnection: " + e.getMessage()
                + " | SQLState=" + e.getSQLState()
                + " | ErrorCode=" + e.getErrorCode());
            throw e;
        }
    }
}
