/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

/**
 * PATRÓN: STRATEGY
 * 
 * Define una familia de algoritmos para calcular descuentos,
 * encapsula cada uno y los hace intercambiables.
 */

/**
 * Interfaz Strategy para calcular descuentos
 */
public interface EstrategiaDescuento {
    /**
     * Calcula el descuento sobre un subtotal
     * @param subtotal Subtotal antes del descuento
     * @return Monto del descuento a aplicar
     */
    double calcularDescuento(double subtotal);
    
    /**
     * Obtiene el porcentaje de descuento
     * @return Porcentaje (0.0 a 1.0)
     */
    double getPorcentajeDescuento();
    
    /**
     * Obtiene el nombre del tipo de descuento
     */
    String getNombre();
}