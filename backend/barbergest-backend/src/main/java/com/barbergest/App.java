
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
            // clienteDAO.registrarCliente("Pedro Gomez", "3105551234");

            // READ
            clienteDAO.listarClientes();

            // UPDATE
            // clienteDAO.actualizarCliente(2, "Pedro Gomez", "3009876543");

            // DELETE
            // clienteDAO.eliminarCliente(3);


            // ==================================
            // MODULO DE BARBEROS
            // ==================================

            BarberoDAO barberoDAO = new BarberoDAO();

            // CREATE
            // barberoDAO.registrarBarbero("Barbero Prueba", "3000000000");

            // READ
            barberoDAO.listarBarberos();

            // UPDATE
            // barberoDAO.actualizarBarbero(
            //     3, "Barbero Actualizado", "3111111111"
            // );

            // DELETE
            // barberoDAO.eliminarBarbero(3);


            // ==================================
            // MODULO DE CITAS
            // ==================================

            CitaDAO citaDAO = new CitaDAO();

            // CREATE
            // citaDAO.registrarCita(
            //     2, 2, 2, "2026-11-15", "10:00:00"
            // );

            // READ
            citaDAO.listarCitas();

            // UPDATE
            // citaDAO.actualizarCita(
            //     2, 2, 2, 2,
            //     "2026-11-15", "11:00:00", "PROGRAMADA"
            // );

            // CANCELAR
            // citaDAO.cancelarCita(2);

            // DELETE
            // citaDAO.eliminarCita(2);


            // ==================================
            // MODULO DE SERVICIOS
            // ==================================

            ServicioDAO servicioDAO = new ServicioDAO();

            // CREATE - Servicio temporal
            // servicioDAO.registrarServicio(
            //     "Servicio Prueba",
            //     "Servicio creado para probar el CRUD",
            //     35,
            //     18000.00
            // );

            // READ
            servicioDAO.listarServicios();

            // UPDATE - Sustituir ID por el generado en CREATE
            // servicioDAO.actualizarServicio(
            //     4,
            //     "Servicio Prueba Actualizado",
            //     "Descripcion actualizada",
            //     45,
            //     20000.00
            // );

            // DELETE - Sustituir ID por el generado en CREATE
            // servicioDAO.eliminarServicio(4);

        } finally {

            AbandonedConnectionCleanupThread.checkedShutdown();

        }
    }
}
