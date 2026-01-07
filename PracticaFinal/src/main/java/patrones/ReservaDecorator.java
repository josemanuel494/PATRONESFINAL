/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

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
    protected Reserva reservaDecorada;
    
    public ReservaDecorator(Reserva reserva) {
        super(reserva.getCodigoReserva(), reserva.getCliente(), 
              reserva.getSesion(), reserva.getNumEntradas());
        this.reservaDecorada = reserva;
    }
    
    @Override
    public abstract double getPrecioExtras();
    
    @Override
    public abstract String getDescripcionExtras();
}