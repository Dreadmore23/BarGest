
package com.barbergest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.dao.DataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarberoDAO {

    private final JdbcTemplate jdbcTemplate = ConexionBD.obtenerJdbcTemplate();

    // READ - Consultar barberos
    public void listarBarberos() {
        // Consulta los registros mediante un procedimiento almacenado.
        try {
            jdbcTemplate.execute("{CALL listar_barberos()}",
                (CallableStatementCallback<Void>) consulta -> {
                    try (ResultSet resultado = consulta.executeQuery()) {
                        System.out.println("=== LISTA DE BARBEROS ===");
                        while (resultado.next()) {
                            System.out.println("id_barbero: " + resultado.getString("id_barbero") + " | " + "nombre: " + resultado.getString("nombre") + " | " + "telefono: " + resultado.getString("telefono"));
                        }
                    }
                    return null;
                });
        } catch (DataAccessException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }

    // CREATE - Registrar barbero
    public void registrarBarbero(String nombre, String telefono) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL registrar_barbero(?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setString(1, nombre);
                    consulta.setString(2, telefono);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "registrarBarbero: operación correcta." : "registrarBarbero: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en registrarBarbero: " + e.getMessage());
        }
    }

    // UPDATE - Actualizar barbero
    public void actualizarBarbero(int id, String nombre, String telefono) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL actualizar_barbero(?, ?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, id);
                    consulta.setString(2, nombre);
                    consulta.setString(3, telefono);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "actualizarBarbero: operación correcta." : "actualizarBarbero: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en actualizarBarbero: " + e.getMessage());
        }
    }

    // DELETE - Eliminar barbero
    public void eliminarBarbero(int id) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL eliminar_barbero(?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, id);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "eliminarBarbero: operación correcta." : "eliminarBarbero: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en eliminarBarbero: " + e.getMessage());
        }
    }
}
