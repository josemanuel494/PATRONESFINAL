/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

/**
 * Estrategia para abono Básico (10% descuento)
 */
public class DescuentoBasico implements EstrategiaDescuento {
    private static final double PORCENTAJE = 0.10;
    
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
        return "Básico (10%)";
    }
}
