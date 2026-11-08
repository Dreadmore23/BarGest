
package com.barbergest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarberoDAO {

    // READ - Consultar barberos
    public void listarBarberos() {

        String sql = "SELECT id_barbero, nombre, telefono FROM barbero";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql);
            ResultSet resultado = consulta.executeQuery()
        ) {

            System.out.println("=== LISTA DE BARBEROS ===");

            while (resultado.next()) {
                int id = resultado.getInt("id_barbero");
                String nombre = resultado.getString("nombre");
                String telefono = resultado.getString("telefono");

                System.out.println(
                    "ID: " + id +
                    " | Nombre: " + nombre +
                    " | Telefono: " + telefono
                );
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // CREATE - Registrar barbero
    public void registrarBarbero(String nombre, String telefono) {

        String sql = "INSERT INTO barbero (nombre, telefono) VALUES (?, ?)";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, nombre);
            consulta.setString(2, telefono);

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Barbero registrado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // UPDATE - Actualizar barbero
    public void actualizarBarbero(int id, String nombre, String telefono) {

        String sql = "UPDATE barbero SET nombre = ?, telefono = ? WHERE id_barbero = ?";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, nombre);
            consulta.setString(2, telefono);
            consulta.setInt(3, id);

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Barbero actualizado correctamente.");
            } else {
                System.out.println("Barbero no encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // DELETE - Eliminar barbero
    public void eliminarBarbero(int id) {

        String sql = "DELETE FROM barbero WHERE id_barbero = ?";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, id);

            int filas = consulta.executeUpdate();

            if (filas > 0) {
                System.out.println("Barbero eliminado correctamente.");
            } else {
                System.out.println("Barbero no encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
