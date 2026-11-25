
package com.barbergest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.dao.DataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioDAO {

    private final JdbcTemplate jdbcTemplate = ConexionBD.obtenerJdbcTemplate();

    // CREATE - Registrar servicio
    public void registrarServicio(String nombre, String descripcion,
                                  int duracion, double precio) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL registrar_servicio(?, ?, ?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setString(1, nombre);
                    consulta.setString(2, descripcion);
                    consulta.setInt(3, duracion);
                    consulta.setDouble(4, precio);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "registrarServicio: operación correcta." : "registrarServicio: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en registrarServicio: " + e.getMessage());
        }
    }

    // READ - Listar servicios
    public void listarServicios() {
        // Consulta los registros mediante un procedimiento almacenado.
        try {
            jdbcTemplate.execute("{CALL listar_servicios()}",
                (CallableStatementCallback<Void>) consulta -> {
                    try (ResultSet resultado = consulta.executeQuery()) {
                        System.out.println("=== LISTA DE SERVICIOS ===");
                        while (resultado.next()) {
                            System.out.println("id_servicio: " + resultado.getString("id_servicio") + " | " + "nombre: " + resultado.getString("nombre") + " | " + "descripcion: " + resultado.getString("descripcion") + " | " + "duracion: " + resultado.getString("duracion") + " | " + "precio: " + resultado.getString("precio"));
                        }
                    }
                    return null;
                });
        } catch (DataAccessException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }

    // UPDATE - Actualizar servicio
    public void actualizarServicio(int idServicio, String nombre,
                                   String descripcion, int duracion,
                                   double precio) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL actualizar_servicio(?, ?, ?, ?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, idServicio);
                    consulta.setString(2, nombre);
                    consulta.setString(3, descripcion);
                    consulta.setInt(4, duracion);
                    consulta.setDouble(5, precio);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "actualizarServicio: operación correcta." : "actualizarServicio: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en actualizarServicio: " + e.getMessage());
        }
    }

    // DELETE - Eliminar servicio
    public void eliminarServicio(int idServicio) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL eliminar_servicio(?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, idServicio);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "eliminarServicio: operación correcta." : "eliminarServicio: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en eliminarServicio: " + e.getMessage());
        }
    }
}
