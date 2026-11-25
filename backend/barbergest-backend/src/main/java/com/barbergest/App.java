
package com.barbergest;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class App {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       SISTEMA BARBERGEST       ");
        System.out.println("================================");

        try {

            // ==================================
            // MODULO DE CLIENTES
            // ==================================

            ClienteDAO clienteDAO = new ClienteDAO();

            // CREATE
            // clienteDAO.registrarCliente("Prueba Spring JDBC", "3000000000");

            // READ
            clienteDAO.listarClientes();

            // UPDATE
            // clienteDAO.actualizarCliente(6, "Cliente Spring Actualizado", "3111111111");

            // DELETE
            // clienteDAO.eliminarCliente(6);


            // ==================================
            // MODULO DE BARBEROS
            // ==================================

            BarberoDAO barberoDAO = new BarberoDAO();

            // CREATE
            // barberoDAO.registrarBarbero("Barbero Prueba Spring", "3000000001");

            // READ
            barberoDAO.listarBarberos();

            // UPDATE
            // barberoDAO.actualizarBarbero(4, "Barbero Spring Actualizado", "3111111112");

            // DELETE
            // barberoDAO.eliminarBarbero(4);


            // ==================================
            // MODULO DE CITAS
            // ==================================

            CitaDAO citaDAO = new CitaDAO();

            // CREATE
            // citaDAO.registrarCita(1, 1, 1, "2026-12-01", "10:00:00");

            // READ
            citaDAO.listarCitas();

            // UPDATE
            // citaDAO.actualizarCita(5, 1, 1, 1, "2026-12-01", "14:00:00", "PROGRAMADA");

            // CANCELAR
            // citaDAO.cancelarCita(3);

            // DELETE
            // citaDAO.eliminarCita(5);


            // ==================================
            // MODULO DE SERVICIOS
            // ==================================

            ServicioDAO servicioDAO = new ServicioDAO();

            // CREATE - Servicio temporal
            // servicioDAO.registrarServicio("Servicio Prueba Spring", "Servicio de prueba", 30, 18000.00);

            // READ
            servicioDAO.listarServicios();

            // UPDATE - Sustituir ID por el generado en CREATE
            // servicioDAO.actualizarServicio(5, "Servicio Spring Actualizado", "Descripcion actualizada", 40, 22000.00);

            // DELETE - Sustituir ID por el generado en CREATE
            // servicioDAO.eliminarServicio(5);

        } finally {

            AbandonedConnectionCleanupThread.checkedShutdown();

        }
    }
}
