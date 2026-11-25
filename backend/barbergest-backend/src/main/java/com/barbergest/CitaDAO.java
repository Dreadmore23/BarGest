
package com.barbergest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.dao.DataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;

public class CitaDAO {

    private final JdbcTemplate jdbcTemplate = ConexionBD.obtenerJdbcTemplate();

    // =====================================
    // READ - CONSULTAR CITAS
    // =====================================

    public void listarCitas() {
        // Consulta los registros mediante un procedimiento almacenado.
        try {
            jdbcTemplate.execute("{CALL listar_citas()}",
                (CallableStatementCallback<Void>) consulta -> {
                    try (ResultSet resultado = consulta.executeQuery()) {
                        System.out.println("=== LISTA DE CITAS ===");
                        while (resultado.next()) {
                            System.out.println("id_cita: " + resultado.getString("id_cita") + " | " + "cliente: " + resultado.getString("cliente") + " | " + "barbero: " + resultado.getString("barbero") + " | " + "servicio: " + resultado.getString("servicio") + " | " + "fecha: " + resultado.getString("fecha") + " | " + "hora: " + resultado.getString("hora") + " | " + "estado: " + resultado.getString("estado"));
                        }
                    }
                    return null;
                });
        } catch (DataAccessException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }

    // =====================================
    // CREATE - REGISTRAR CITA
    // =====================================

    public void registrarCita(
            int idCliente,
            int idBarbero,
            int idServicio,
            String fecha,
            String hora) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL registrar_cita(?, ?, ?, ?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, idCliente);
                    consulta.setInt(2, idBarbero);
                    consulta.setInt(3, idServicio);
                    consulta.setDate(4, Date.valueOf(fecha));
                    consulta.setTime(5, Time.valueOf(hora));
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "registrarCita: operación correcta." : "registrarCita: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en registrarCita: " + e.getMessage());
        }
    }

    // =====================================
    // UPDATE - ACTUALIZAR CITA
    // =====================================

    public void actualizarCita(
            int idCita,
            int idCliente,
            int idBarbero,
            int idServicio,
            String fecha,
            String hora,
            String estado) {
        if (!estadoValido(estado)) {
            System.out.println("Estado no valido.");
            return;
        }
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL actualizar_cita(?, ?, ?, ?, ?, ?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, idCita);
                    consulta.setInt(2, idCliente);
                    consulta.setInt(3, idBarbero);
                    consulta.setInt(4, idServicio);
                    consulta.setDate(5, Date.valueOf(fecha));
                    consulta.setTime(6, Time.valueOf(hora));
                    consulta.setString(7, estado);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "actualizarCita: operación correcta." : "actualizarCita: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en actualizarCita: " + e.getMessage());
        }
    }

    // =====================================
    // CANCELAR CITA
    // =====================================

    public void cancelarCita(int idCita) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL cancelar_cita(?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, idCita);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "cancelarCita: operación correcta." : "cancelarCita: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en cancelarCita: " + e.getMessage());
        }
    }

    // =====================================
    // DELETE - ELIMINAR CITA
    // =====================================

    public void eliminarCita(int idCita) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL eliminar_cita(?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, idCita);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "eliminarCita: operación correcta." : "eliminarCita: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en eliminarCita: " + e.getMessage());
        }
    }

    // =====================================
    // VALIDAR ESTADOS
    // =====================================

    private boolean estadoValido(String estado) {

        return "PROGRAMADA".equals(estado)
            || "CANCELADA".equals(estado)
            || "REALIZADA".equals(estado);
    }
}
