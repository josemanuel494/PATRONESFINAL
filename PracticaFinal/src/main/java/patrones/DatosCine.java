/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;
import java.io.Serializable;
import java.util.*;

/**
 * Clase contenedora de todos los datos del sistema
 * Serializable para persistencia
 */
public class DatosCine implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Pelicula> peliculas;
    private List<Sesion> sesiones;
    private List<Cliente> clientes;
    private List<Reserva> reservas;
    private List<Sala> salas;
    
    public DatosCine() {
        this.peliculas = new ArrayList<>();
        this.sesiones = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.salas = new ArrayList<>();
    }
    
    // Getters
    public List<Pelicula> getPeliculas() {
        return peliculas;
    }
    
    public List<Sesion> getSesiones() {
        return sesiones;
    }
    
    public List<Cliente> getClientes() {
        return clientes;
    }
    
    public List<Reserva> getReservas() {
        return reservas;
    }
    
    public List<Sala> getSalas() {
        return salas;
    }
    
    /**
     * Inicializa las 3 salas del cine según requisitos
     */
    public void inicializarSalas() {
        salas.clear();
        salas.add(new Sala(1, 80, false, true));   // Sala 1: IMAX
        salas.add(new Sala(2, 50, false, false));  // Sala 2: Normal
        salas.add(new Sala(3, 30, true, false));   // Sala VIP
    }
    
    /**
     * Inicializa datos de prueba
     */
    public void inicializarDatosPrueba() {
        // Inicializar salas
        inicializarSalas();
        
        // Crear empleado admin
        Cliente admin = new Cliente("00000000A", "Administrador", 
                                   "admin@ocine.com", "999999999", 
                                   "admin", TipoAbono.VIP);
        clientes.add(admin);
        
        // Crear algunos clientes de prueba
        clientes.add(new Cliente("12345678A", "Juan Pérez", 
                                "juan@email.com", "611111111", 
                                "1234", TipoAbono.BASICO));
        
        clientes.add(new Cliente("87654321B", "María García", 
                                "maria@email.com", "622222222", 
                                "1234", TipoAbono.PREMIUM));
        
        clientes.add(new Cliente("11111111C", "Carlos López", 
                                "carlos@email.com", "633333333", 
                                "1234", TipoAbono.VIP));
        
        System.out.println("Datos de prueba inicializados");
    }
}