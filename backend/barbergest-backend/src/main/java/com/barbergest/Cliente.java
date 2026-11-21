
package com.barbergest;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String telefono;

    // Constructor
    public Cliente(int idCliente, String nombre, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // Obtener ID del cliente
    public int getIdCliente() {
        return idCliente;
    }

    // Obtener nombre del cliente
    public String getNombre() {
        return nombre;
    }

    // Obtener teléfono del cliente
    public String getTelefono() {
        return telefono;
    }
}
