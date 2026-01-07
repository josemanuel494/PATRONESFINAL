/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Enumeración para el estado de las reservas
 */
public enum EstadoReserva {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada");
    
    private final String nombre;
    
    EstadoReserva(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
