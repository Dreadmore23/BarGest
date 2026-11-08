
package com.barbergest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioDAO {

    // CREATE - Registrar servicio
    public void registrarServicio(String nombre, String descripcion,
                                  int duracion, double precio) {

        String sql = "INSERT INTO servicio " +
                     "(nombre, descripcion, duracion, precio) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, duracion);
            ps.setDouble(4, precio);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Servicio registrado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar servicio: " + e.getMessage());
        }
    }

    // READ - Listar servicios
    public void listarServicios() {

        String sql = "SELECT id_servicio, nombre, descripcion, " +
                     "duracion, precio FROM servicio ORDER BY id_servicio";

        System.out.println("\n=== LISTA DE SERVICIOS ===");

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                    "ID: " + rs.getInt("id_servicio") +
                    " | Nombre: " + rs.getString("nombre") +
                    " | Descripcion: " + rs.getString("descripcion") +
                    " | Duracion: " + rs.getInt("duracion") + " min" +
                    " | Precio: $" + rs.getBigDecimal("precio")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar servicios: " + e.getMessage());
        }
    }

    // UPDATE - Actualizar servicio
    public void actualizarServicio(int idServicio, String nombre,
                                   String descripcion, int duracion,
                                   double precio) {

        String sql = "UPDATE servicio SET nombre = ?, descripcion = ?, " +
                     "duracion = ?, precio = ? WHERE id_servicio = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, duracion);
            ps.setDouble(4, precio);
            ps.setInt(5, idServicio);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Servicio actualizado correctamente.");
            } else {
                System.out.println("No se encontro el servicio.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar servicio: " + e.getMessage());
        }
    }

    // DELETE - Eliminar servicio
    public void eliminarServicio(int idServicio) {

        String sql = "DELETE FROM servicio WHERE id_servicio = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idServicio);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Servicio eliminado correctamente.");
            } else {
                System.out.println("No se encontro el servicio.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar servicio: " + e.getMessage());
        }
    }
}
