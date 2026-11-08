
package com.barbergest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {
    //READ - Consultar cliente
    public void listarClientes() {

        String sql = "SELECT id_cliente, nombre, telefono FROM cliente";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql);
            ResultSet resultado = consulta.executeQuery()
        ) {

            System.out.println("=== LISTA DE CLIENTES ===");

            while (resultado.next()) {

                int id = resultado.getInt("id_cliente");
                String nombre = resultado.getString("nombre");
                String telefono = resultado.getString("telefono");

                System.out.println(
                    "ID: " + id +
                    " | Nombre: " + nombre +
                    " | Telefono: " + telefono
                );
            }

        } catch (SQLException e) {

            System.out.println(
                "Error al consultar clientes: " + e.getMessage()
            );
        }
    }
    //CREATE - Registrar cliente
    public void registrarCliente(String nombre, String telefono) {

        String sql = "INSERT INTO cliente (nombre, telefono) VALUES (?, ?)";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, nombre);
            consulta.setString(2, telefono);

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente registrado correctamente.");
            }

        } catch (SQLException e) {

            System.out.println(
                "Error al registrar cliente: " + e.getMessage()
            );
        }
    }
    //UPDATE - Actualizar cliente 
    public void actualizarCliente(int id, String nombre, String telefono) {

        String sql = "UPDATE cliente SET nombre = ?, telefono = ? WHERE id_cliente = ?";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, nombre);
            consulta.setString(2, telefono);
            consulta.setInt(3, id);

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente actualizado correctamente.");
            } else {
                System.out.println("No se encontro un cliente con ese ID.");
            }

        } catch (SQLException e) {

            System.out.println(
                "Error al actualizar cliente: " + e.getMessage()
            );
        }
    }
    //DELETE - Eliminar cliente
    public void eliminarCliente(int id) {

    String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (
            Connection conexion = ConexionBD.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, id);

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado correctamente.");
            } else {
                System.out.println("No se encontro un cliente con ese ID.");
            }

        } catch (SQLException e) {

            System.out.println(
                "Error al eliminar cliente: " + e.getMessage()
            );
        }
    }

}
