/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una reserva básica (sin extras)
 * Esta clase será decorada para añadir extras de comida
 */
public class Reserva implements Serializable, ObservadorSesion {
    private static final long serialVersionUID = 1L;
    
    private String codigoReserva;
    private Cliente cliente;
    private Sesion sesion;
    private int numEntradas;
    private LocalDateTime fechaHoraReserva;
    private EstadoReserva estado;
    private double precioFinal;
    
    /**
     * Constructor de Reserva
     */
    public Reserva(String codigoReserva, Cliente cliente, Sesion sesion, int numEntradas) {
        this(codigoReserva, cliente, sesion, numEntradas, true);
    }

    /**
     * Constructor interno para evitar re-registrar observadores al decorar.
     */
    protected Reserva(String codigoReserva, Cliente cliente, Sesion sesion, int numEntradas,
                      boolean registrarObservador) {
        this.codigoReserva = codigoReserva;
        this.cliente = cliente;
        this.sesion = sesion;
        this.numEntradas = numEntradas;
        this.fechaHoraReserva = LocalDateTime.now();
        this.estado = EstadoReserva.PENDIENTE;
        this.precioFinal = 0.0;

        if (registrarObservador && sesion != null) {
            sesion.agregarObservador(this);
        }
    }

    /**
     * Calcula el precio base de las entradas (sin extras)
     * Precio entrada = Precio base sesión + Recargo proyección
     */
    public double calcularPrecioEntradas() {
        double precioEntrada = sesion.calcularPrecioEntrada();
        return precioEntrada * numEntradas;
    }
    
    /**
     * Obtiene el precio de los extras (0 en la reserva base)
     * Será sobrescrito por los decoradores
     */
    public double getPrecioExtras() {
        return 0.0;
    }
    
    /**
     * Obtiene la descripción de extras (vacía en reserva base)
     */
    public String getDescripcionExtras() {
        return "Sin extras";
    }
    
    /**
     * Calcula y establece el precio final con descuento
     * @param descuento Monto del descuento a aplicar
     */
    public void calcularPrecioFinal(double descuento) {
        double subtotal = calcularPrecioEntradas();
        double subtotalConDescuento = subtotal - descuento;
        this.precioFinal = subtotalConDescuento + getPrecioExtras();
    }
    
    /**
     * Confirma la reserva
     */
    public void confirmar() {
        this.estado = EstadoReserva.CONFIRMADA;
    }
    
    /**
     * Cancela la reserva y libera las plazas
     */
    public void cancelar() {
        if (estado != EstadoReserva.CANCELADA) {
            this.estado = EstadoReserva.CANCELADA;
            sesion.liberarPlazas(numEntradas);
            sesion.eliminarObservador(this);
        }
    }
    
    /**
     * Implementación del patrón Observer
     * Se ejecuta cuando la sesión notifica cambios
     */
    @Override
    public void actualizar(Sesion sesion, String mensaje) {
        // Si la sesión fue cancelada, cancelar automáticamente la reserva
        if (sesion.getEstado() == EstadoSesion.CANCELADA) {
            if (this.estado != EstadoReserva.CANCELADA) {
                this.estado = EstadoReserva.CANCELADA;
                System.out.println("Reserva " + codigoReserva + " cancelada automáticamente: " + mensaje);
            }
        }
    }
    
    // Getters
    public String getCodigoReserva() {
        return codigoReserva;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public Sesion getSesion() {
        return sesion;
    }
    
    public int getNumEntradas() {
        return numEntradas;
    }
    
    public LocalDateTime getFechaHoraReserva() {
        return fechaHoraReserva;
    }
    
    public EstadoReserva getEstado() {
        return estado;
    }
    
    public double getPrecioFinal() {
        return precioFinal;
    }
    
    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }
    
    @Override
    public String toString() {
        return "Reserva " + codigoReserva + " - " + cliente.getNombreCompleto() + 
               " - " + sesion.getPelicula().getTitulo() + " (" + numEntradas + " entradas)";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reserva reserva = (Reserva) obj;
        return codigoReserva.equals(reserva.codigoReserva);
    }
    
    @Override
    public int hashCode() {
        return codigoReserva.hashCode();
    }
}
