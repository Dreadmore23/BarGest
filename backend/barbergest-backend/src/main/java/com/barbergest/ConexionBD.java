
package com.barbergest;

import java.sql.Connection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.AbstractDataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3307/barberia";

    public static Connection conectar() throws SQLException {

        String usuario = System.getenv("BARBERGEST_DB_USER");
        String contrasena = System.getenv("BARBERGEST_DB_PASSWORD");

        // Verificar las variables de entorno
        if (usuario == null || contrasena == null) {
            throw new SQLException(
                "Faltan las variables de entorno de MySQL."
            );
        }

        // Cargar el controlador JDBC de MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "No se encontró el controlador JDBC de MySQL.",
                e
            );
        }

        // Establecer la conexión con MySQL
        return DriverManager.getConnection(
            URL, usuario, contrasena
        );
    }
    // Spring JDBC reutiliza la conexión existente y administra sus recursos.
    public static JdbcTemplate obtenerJdbcTemplate() {
        return new JdbcTemplate(new AbstractDataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return conectar();
            }

            @Override
            public Connection getConnection(String usuario, String contrasena)
                    throws SQLException {
                return conectar();
            }
        });
    }
}
