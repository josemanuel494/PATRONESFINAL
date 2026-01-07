/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

/**
 * Estrategia para abono VIP (40% descuento)
 */
public class DescuentoVIP implements EstrategiaDescuento {
    private static final double PORCENTAJE = 0.40;
    
    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * PORCENTAJE;
    }
    
    @Override
    public double getPorcentajeDescuento() {
        return PORCENTAJE;
    }
    
    @Override
    public String getNombre() {
        return "VIP (40%)";
    }
}
