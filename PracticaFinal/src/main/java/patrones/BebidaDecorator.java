/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.Reserva;

/**
 * Decorador concreto: Añade bebida (2€)
 */
public class BebidaDecorator extends ReservaDecorator {
    private static final long serialVersionUID = 1L;
    private static final double PRECIO = 2.0;
    
    public BebidaDecorator(Reserva reserva) {
        super(reserva);
    }
    
    @Override
    public double getPrecioExtras() {
        return reservaDecorada.getPrecioExtras() + PRECIO;
    }
    
    @Override
    public String getDescripcionExtras() {
        String extrasBase = reservaDecorada.getDescripcionExtras();
        if (extrasBase.equals("Sin extras")) {
            return "Bebida";
        }
        return extrasBase + ", Bebida";
    }
}