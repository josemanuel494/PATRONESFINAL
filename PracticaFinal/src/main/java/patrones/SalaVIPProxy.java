/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;
import excepciones.*;

/**
 * PATRÓN: PROXY
 * 
 * Controla el acceso a las salas VIP.
 * Verifica que el cliente tenga abono VIP antes de permitir reservas.
 */

/**
 * Proxy para sala VIP - controla el acceso
 */
public class SalaVIPProxy implements ISala {
    private Sala sala;
    
    public SalaVIPProxy(Sala sala) {
        if (!sala.isEsVIP()) {
            throw new IllegalArgumentException("Esta sala no es VIP");
        }
        this.sala = sala;
    }
    
    /**
     * Verifica que el cliente tenga abono VIP antes de permitir acceso
     */
    @Override
    public void verificarAcceso(Cliente cliente) throws AccesoSalaVIPDenegadoException {
        if (!cliente.tieneAccesoVIP()) {
            throw new AccesoSalaVIPDenegadoException(
                "Acceso denegado. La Sala VIP requiere abono VIP. " +
                "Su abono actual: " + cliente.getTipoAbono()
            );
        }
        // Si tiene acceso VIP, no se lanza excepción
    }
    
    @Override
    public Sala getSala() {
        return sala;
    }
}