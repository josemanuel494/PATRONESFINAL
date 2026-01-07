/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una sesión de proyección
 * PATRÓN: OBSERVER (Subject) - Notifica cambios a observadores
 */
public class Sesion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigoSesion;
    private Pelicula pelicula;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private Sala sala;
    private double precioBase;
    private int plazasDisponibles;
    private EstadoSesion estado;
    
    // Lista de observadores (clientes con reservas)
    private transient List<ObservadorSesion> observadores;
    
    /**
     * Constructor de Sesión
     */
    public Sesion(String codigoSesion, Pelicula pelicula, LocalDate fecha,
                  LocalTime horaInicio, Sala sala, double precioBase) {
        this.codigoSesion = codigoSesion;
        this.pelicula = pelicula;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.sala = sala;
        this.precioBase = precioBase;
        this.plazasDisponibles = sala.getCapacidad();
        this.estado = EstadoSesion.PROGRAMADA;
        this.observadores = new ArrayList<>();
    }
    
    /**
     * Registra un observador
     */
    public void agregarObservador(ObservadorSesion observador) {
        if (observadores == null) {
            observadores = new ArrayList<>();
        }
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    /**
     * Elimina un observador
     */
    public void eliminarObservador(ObservadorSesion observador) {
        if (observadores != null) {
            observadores.remove(observador);
        }
    }
    
    /**
     * Notifica a todos los observadores sobre un cambio
     */
    private void notificarObservadores(String mensaje) {
        if (observadores != null) {
            for (ObservadorSesion obs : observadores) {
                obs.actualizar(this, mensaje);
            }
        }
    }
    
    /**
     * Reserva un número de plazas
     * @param numEntradas Número de entradas a reservar
     * @return true si se pudo reservar, false si no hay plazas
     */
    public boolean reservarPlazas(int numEntradas) {
        if (plazasDisponibles >= numEntradas && estado == EstadoSesion.PROGRAMADA) {
            plazasDisponibles -= numEntradas;
            notificarObservadores("Plazas actualizadas. Disponibles: " + plazasDisponibles);
            return true;
        }
        return false;
    }
    
    /**
     * Libera plazas (cuando se cancela una reserva)
     */
    public void liberarPlazas(int numEntradas) {
        plazasDisponibles += numEntradas;
        notificarObservadores("Plazas liberadas. Disponibles: " + plazasDisponibles);
    }
    
    /**
     * Cancela la sesión y notifica a observadores
     */
    public void cancelar() {
        this.estado = EstadoSesion.CANCELADA;
        notificarObservadores("SESIÓN CANCELADA: " + pelicula.getTitulo() + " - " + fecha + " " + horaInicio);
    }
    
    /**
     * Calcula el precio total de una entrada para esta sesión
     */
    public double calcularPrecioEntrada() {
        return precioBase + pelicula.getRecargoProyeccion();
    }
    
    /**
     * Verifica si la sesión está disponible para reservar
     */
    public boolean estaDisponible() {
        return estado == EstadoSesion.PROGRAMADA && plazasDisponibles > 0;
    }
    
    // Getters
    public String getCodigoSesion() {
        return codigoSesion;
    }
    
    public Pelicula getPelicula() {
        return pelicula;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public Sala getSala() {
        return sala;
    }
    
    public double getPrecioBase() {
        return precioBase;
    }
    
    public int getPlazasDisponibles() {
        return plazasDisponibles;
    }
    
    public EstadoSesion getEstado() {
        return estado;
    }
    
    public int getPlazasOcupadas() {
        return sala.getCapacidad() - plazasDisponibles;
    }
    
    // Setters
    public void setEstado(EstadoSesion estado) {
        this.estado = estado;
        notificarObservadores("Estado cambiado a: " + estado);
    }
    
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }
    
    @Override
    public String toString() {
        return pelicula.getTitulo() + " - " + fecha + " " + horaInicio + 
               " (Sala " + sala.getNumero() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sesion sesion = (Sesion) obj;
        return codigoSesion.equals(sesion.codigoSesion);
    }
    
    @Override
    public int hashCode() {
        return codigoSesion.hashCode();
    }
}

