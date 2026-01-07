/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

/**
 * Estrategia para abono Premium (25% descuento)
 */
public class DescuentoPremium implements EstrategiaDescuento {
    private static final double PORCENTAJE = 0.25;
    
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
        return "Premium (25%)";
    }
}
