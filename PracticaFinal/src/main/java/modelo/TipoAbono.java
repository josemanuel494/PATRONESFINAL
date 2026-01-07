/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Enumeración para los tipos de abono
 */
public enum TipoAbono {
    BASICO("Básico", 5.0, 0.10),
    PREMIUM("Premium", 15.0, 0.25),
    VIP("VIP", 30.0, 0.40);
    
    private final String nombre;
    private final double cuotaMensual;
    private final double porcentajeDescuento;
    
    TipoAbono(String nombre, double cuota, double descuento) {
        this.nombre = nombre;
        this.cuotaMensual = cuota;
        this.porcentajeDescuento = descuento;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getCuotaMensual() {
        return cuotaMensual;
    }
    
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
