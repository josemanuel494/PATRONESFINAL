/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.Reserva;

/**
 * Decorador concreto: Añade combo palomitas+bebida (4€)
 */
public class ComboDecorator extends ReservaDecorator {
    private static final double PRECIO = 4.0;
    
    public ComboDecorator(Reserva reserva) {
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
            return "Combo (Palomitas + Bebida)";
        }
        return extrasBase + ", Combo";
    }
}