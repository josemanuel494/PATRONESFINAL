/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Enumeración para el estado de las sesiones
 */
public enum EstadoSesion {
    PROGRAMADA("Programada"),
    EN_CURSO("En curso"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");
    
    private final String nombre;
    
    EstadoSesion(String nombre) {
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
