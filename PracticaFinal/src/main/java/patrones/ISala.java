/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package patrones;

import modelo.*;
import excepciones.*;

/**
 * Interfaz común para sala real y proxy
 */
public interface ISala {
    /**
     * Verifica si un cliente puede acceder a esta sala
     * @param cliente Cliente a verificar
     * @throws AccesoSalaVIPDenegadoException si no tiene acceso
     */
    void verificarAcceso(Cliente cliente) throws AccesoSalaVIPDenegadoException;
    
    /**
     * Obtiene información de la sala
     */
    Sala getSala();
}
