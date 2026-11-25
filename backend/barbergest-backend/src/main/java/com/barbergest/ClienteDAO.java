
package com.barbergest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.dao.DataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final JdbcTemplate jdbcTemplate = ConexionBD.obtenerJdbcTemplate();
    //READ - Consultar cliente
    public void listarClientes() {
        // Consulta los registros mediante un procedimiento almacenado.
        try {
            jdbcTemplate.execute("{CALL listar_clientes()}",
                (CallableStatementCallback<Void>) consulta -> {
                    try (ResultSet resultado = consulta.executeQuery()) {
                        System.out.println("=== LISTA DE CLIENTES ===");
                        while (resultado.next()) {
                            System.out.println("id_cliente: " + resultado.getString("id_cliente") + " | " + "nombre: " + resultado.getString("nombre") + " | " + "telefono: " + resultado.getString("telefono"));
                        }
                    }
                    return null;
                });
        } catch (DataAccessException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }

    // READ - Obtener clientes para mostrar en la página web
    public List<Cliente> obtenerClientes() throws SQLException {
        // Convierte el resultado del procedimiento en objetos para el Servlet.
        try {
            return jdbcTemplate.execute("{CALL listar_clientes()}",
                (CallableStatementCallback<List<Cliente>>) consulta -> {
                    List<Cliente> clientes = new ArrayList<>();
                    try (ResultSet resultado = consulta.executeQuery()) {
                        while (resultado.next()) {
                            clientes.add(new Cliente(resultado.getInt("id_cliente"),
                                resultado.getString("nombre"), resultado.getString("telefono")));
                        }
                    }
                    return clientes;
                });
        } catch (DataAccessException e) {
            throw new SQLException("Error al consultar clientes", e);
        }
    }

    //CREATE - Registrar cliente
    
public boolean registrarCliente(String nombre, String telefono) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL registrar_cliente(?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setString(1, nombre);
                    consulta.setString(2, telefono);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "registrarCliente: operación correcta." : "registrarCliente: sin filas afectadas.");
            return filas > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en registrarCliente: " + e.getMessage());
            return false;
        }
    }

    //UPDATE - Actualizar cliente 
    public void actualizarCliente(int id, String nombre, String telefono) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL actualizar_cliente(?, ?, ?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, id);
                    consulta.setString(2, nombre);
                    consulta.setString(3, telefono);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "actualizarCliente: operación correcta." : "actualizarCliente: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en actualizarCliente: " + e.getMessage());
        }
    }
    //DELETE - Eliminar cliente
    public void eliminarCliente(int id) {
        // Spring prepara la llamada y libera los recursos JDBC al finalizar.
        try {
            int filas = jdbcTemplate.execute("{CALL eliminar_cliente(?)}",
                (CallableStatementCallback<Integer>) consulta -> {
                    // Los parámetros siguen el orden declarado en MySQL.
                    consulta.setInt(1, id);
                    return consulta.executeUpdate();
                });
            System.out.println(filas > 0 ? "eliminarCliente: operación correcta." : "eliminarCliente: sin filas afectadas.");
        } catch (DataAccessException | IllegalArgumentException e) {
            System.out.println("Error en eliminarCliente: " + e.getMessage());
        }
    }

}
