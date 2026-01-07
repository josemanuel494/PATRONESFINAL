/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.Reserva;

/**
 * Decorador concreto: Añade palomitas (3€)
 */
public class PalomitasDecorator extends ReservaDecorator {
    private static final double PRECIO = 3.0;
    
    public PalomitasDecorator(Reserva reserva) {
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
            return "Palomitas";
        }
        return extrasBase + ", Palomitas";
    }
}