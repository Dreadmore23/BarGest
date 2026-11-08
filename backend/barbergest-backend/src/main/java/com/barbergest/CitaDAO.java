
package com.barbergest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;

public class CitaDAO {

    // =====================================
    // READ - CONSULTAR CITAS
    // =====================================

    public void listarCitas() {

        String sql = """
            SELECT c.id_cita,
                   cl.nombre AS cliente,
                   b.nombre AS barbero,
                   s.nombre AS servicio,
                   c.fecha,
                   c.hora,
                   c.estado
            FROM cita c
            INNER JOIN cliente cl ON c.id_cliente = cl.id_cliente
            INNER JOIN barbero b ON c.id_barbero = b.id_barbero
            INNER JOIN servicio s ON c.id_servicio = s.id_servicio
            ORDER BY c.fecha, c.hora
            """;

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql);
            ResultSet resultado = consulta.executeQuery()
        ) {

            System.out.println("=== LISTA DE CITAS ===");

            while (resultado.next()) {

                System.out.println(
                    "ID: " + resultado.getInt("id_cita") +
                    " | Cliente: " + resultado.getString("cliente") +
                    " | Barbero: " + resultado.getString("barbero") +
                    " | Servicio: " + resultado.getString("servicio") +
                    " | Fecha: " + resultado.getDate("fecha") +
                    " | Hora: " + resultado.getTime("hora") +
                    " | Estado: " + resultado.getString("estado")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar citas: " + e.getMessage());
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

        String sql = """
            INSERT INTO cita
            (id_cliente, id_barbero, id_servicio, fecha, hora, estado)
            VALUES (?, ?, ?, ?, ?, 'PROGRAMADA')
            """;

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, idCliente);
            consulta.setInt(2, idBarbero);
            consulta.setInt(3, idServicio);
            consulta.setDate(4, Date.valueOf(fecha));
            consulta.setTime(5, Time.valueOf(hora));

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Cita registrada correctamente.");
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error al registrar cita: " + e.getMessage());
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

        String sql = """
            UPDATE cita
            SET id_cliente = ?,
                id_barbero = ?,
                id_servicio = ?,
                fecha = ?,
                hora = ?,
                estado = ?
            WHERE id_cita = ?
            """;

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, idCliente);
            consulta.setInt(2, idBarbero);
            consulta.setInt(3, idServicio);
            consulta.setDate(4, Date.valueOf(fecha));
            consulta.setTime(5, Time.valueOf(hora));
            consulta.setString(6, estado);
            consulta.setInt(7, idCita);

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Cita actualizada correctamente.");
            } else {
                System.out.println("Cita no encontrada.");
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error al actualizar cita: " + e.getMessage());
        }
    }

    // =====================================
    // CANCELAR CITA
    // =====================================

    public void cancelarCita(int idCita) {

        String sql = """
            UPDATE cita
            SET estado = 'CANCELADA'
            WHERE id_cita = ? AND estado = 'PROGRAMADA'
            """;

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, idCita);

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Cita cancelada correctamente.");
            } else {
                System.out.println("No existe una cita programada con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al cancelar cita: " + e.getMessage());
        }
    }

    // =====================================
    // DELETE - ELIMINAR CITA
    // =====================================

    public void eliminarCita(int idCita) {

        String sql = "DELETE FROM cita WHERE id_cita = ?";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, idCita);

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Cita eliminada correctamente.");
            } else {
                System.out.println("Cita no encontrada.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar cita: " + e.getMessage());
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
