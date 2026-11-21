<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.barbergest.Cliente" %>

<%
    // Recuperar los clientes enviados por el Servlet
    List<Cliente> clientes =
        (List<Cliente>) request.getAttribute("listaClientes");

    // Recuperar el mensaje de registro exitoso
    String mensaje =
        (String) request.getAttribute("mensaje");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clientes - BarberGest</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background-color: #f5f5f5;
        }

        h1 {
            color: #222;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            margin-top: 20px;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #222;
            color: white;
        }

        .mensaje {
            color: green;
            font-weight: bold;
        }

        .enlaces {
            margin-top: 20px;
        }

        .enlaces a {
            margin-right: 20px;
        }
    </style>
</head>

<body>

    <h1>BarberGest</h1>
    <h2>Módulo de Clientes</h2>

    <% if (mensaje != null) { %>
        <p class="mensaje"><%= mensaje %></p>
    <% } %>

    <h3>Clientes registrados</h3>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Teléfono</th>
            </tr>
        </thead>

        <tbody>
            <% if (clientes != null && !clientes.isEmpty()) { %>

                <% for (Cliente cliente : clientes) { %>
                    <tr>
                        <td><%= cliente.getIdCliente() %></td>
                        <td><%= cliente.getNombre() %></td>
                        <td><%= cliente.getTelefono() %></td>
                    </tr>
                <% } %>

            <% } else { %>
                <tr>
                    <td colspan="3">
                        No hay clientes registrados.
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <div class="enlaces">
        <a href="registrar-cliente.html">
            Registrar nuevo cliente
        </a>

        <a href="index.jsp">
            Volver al inicio
        </a>
    </div>

</body>
</html>
