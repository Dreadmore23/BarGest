
package com.barbergest.servlet;

import com.barbergest.Cliente;
import com.barbergest.ClienteDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    // GET - Consultar clientes y mostrarlos mediante JSP
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ClienteDAO clienteDAO = new ClienteDAO();

        try {

            // Consultar los clientes almacenados en MySQL
            List<Cliente> clientes = clienteDAO.obtenerClientes();

            // Guardar la lista para que JSP pueda utilizarla
            request.setAttribute("listaClientes", clientes);

            // Comprobar si venimos de un registro exitoso
            String registro = request.getParameter("registro");

            if ("exitoso".equals(registro)) {
                request.setAttribute(
                    "mensaje",
                    "Cliente registrado correctamente."
                );
            }

            // Enviar los datos a la página JSP
            request.getRequestDispatcher("/clientes.jsp")
                   .forward(request, response);

        } catch (SQLException e) {

            System.err.println(
                "Error al consultar clientes: " + e.getMessage()
            );

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "No se pudieron consultar los clientes."
            );
        }
    }

    // POST - Registrar clientes en MySQL
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Configurar caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // Recibir los datos del formulario HTML
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");

        // Validar que los campos no estén vacíos
        if (nombre == null || nombre.trim().isEmpty()
                || telefono == null || telefono.trim().isEmpty()) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "El nombre y el teléfono son obligatorios."
            );

            return;
        }

        // Crear objeto de acceso a la base de datos
        ClienteDAO clienteDAO = new ClienteDAO();

        // Registrar el cliente utilizando JDBC
        boolean registrado = clienteDAO.registrarCliente(
            nombre.trim(),
            telefono.trim()
        );

        // Comprobar el resultado del registro
        if (registrado) {

            response.sendRedirect(
                request.getContextPath()
                + "/clientes?registro=exitoso"
            );

        } else {

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "No se pudo registrar el cliente en la base de datos."
            );
        }
    }
}
