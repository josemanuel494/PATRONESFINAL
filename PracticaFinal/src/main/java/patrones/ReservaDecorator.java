/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import java.time.LocalDateTime;
import modelo.*;

/**
 * PATRÓN: DECORATOR
 * 
 * Añade dinámicamente extras de comida a las reservas sin modificar
 * la clase base Reserva. Permite combinar múltiples extras.
 */

/**
 * Decorador abstracto de Reserva
 * Extiende Reserva para mantener compatibilidad
 */
public abstract class ReservaDecorator extends Reserva {

    private static final long serialVersionUID = 1L;

    protected Reserva reservaDecorada;

    public ReservaDecorator(Reserva reserva) {
        super(reserva.getCodigoReserva(), reserva.getCliente(),
              reserva.getSesion(), reserva.getNumEntradas(), false);
        this.reservaDecorada = reserva;
    }

    // Delegación: datos base
    @Override public String getCodigoReserva() { return reservaDecorada.getCodigoReserva(); }
    @Override public Cliente getCliente() { return reservaDecorada.getCliente(); }
    @Override public Sesion getSesion() { return reservaDecorada.getSesion(); }
    @Override public int getNumEntradas() { return reservaDecorada.getNumEntradas(); }
    @Override public LocalDateTime getFechaHoraReserva() { return reservaDecorada.getFechaHoraReserva(); }

    // Delegación: estado
    @Override public EstadoReserva getEstado() { return reservaDecorada.getEstado(); }
    @Override public void confirmar() { reservaDecorada.confirmar(); }
    @Override public void cancelar() { reservaDecorada.cancelar(); }

    // Delegación: observer
    @Override
    public void actualizar(Sesion sesion, String mensaje) {
        reservaDecorada.actualizar(sesion, mensaje);
    }

    // Delegación: precios
    @Override public double calcularPrecioEntradas() { return reservaDecorada.calcularPrecioEntradas(); }
    @Override public double getPrecioFinal() { return reservaDecorada.getPrecioFinal(); }
    @Override public void setPrecioFinal(double precioFinal) { reservaDecorada.setPrecioFinal(precioFinal); }

    @Override
    public void calcularPrecioFinal(double descuento) {
        double subtotal = reservaDecorada.calcularPrecioEntradas();
        double total = (subtotal - descuento) + getPrecioExtras();
        reservaDecorada.setPrecioFinal(total);
    }

    // Los extras los implementan los decoradores concretos
    @Override public abstract double getPrecioExtras();
    @Override public abstract String getDescripcionExtras();
}