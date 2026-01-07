/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.Sala;

/**
 * Factory para crear el tipo correcto de sala según sus características
 */
public class SalaFactory {
    
    /**
     * Crea el wrapper apropiado para una sala
     * @param sala Sala a envolver
     * @return ISala (proxy si es VIP, sala real si es normal)
     */
    public static ISala crearSala(Sala sala) {
        if (sala.isEsVIP()) {
            return new SalaVIPProxy(sala);
        } else {
            return new SalaReal(sala);
        }
    }
}
