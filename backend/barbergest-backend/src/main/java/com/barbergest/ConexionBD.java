
package com.barbergest;

import java.sql.Connection;
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
}
