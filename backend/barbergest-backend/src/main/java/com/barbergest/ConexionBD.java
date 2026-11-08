
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

        if (usuario == null || contrasena == null) {
            throw new SQLException(
                "Faltan las variables de entorno de MySQL."
            );
        }

        return DriverManager.getConnection(
            URL, usuario, contrasena
        );
    }
}
