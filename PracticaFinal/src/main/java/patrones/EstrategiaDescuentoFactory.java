/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.TipoAbono;

/**
 * Factory para obtener la estrategia según el tipo de abono
 */
public class EstrategiaDescuentoFactory {
    
    /**
     * Obtiene la estrategia de descuento apropiada según el tipo de abono
     * @param tipoAbono Tipo de abono del cliente
     * @return Estrategia de descuento correspondiente
     */
    public static EstrategiaDescuento getEstrategia(TipoAbono tipoAbono) {
        switch (tipoAbono) {
            case BASICO:
                return new DescuentoBasico();
            case PREMIUM:
                return new DescuentoPremium();
            case VIP:
                return new DescuentoVIP();
            default:
                throw new IllegalArgumentException("Tipo de abono no válido");
        }
    }
}
