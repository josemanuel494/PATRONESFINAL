/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patrones;

import modelo.*;
import excepciones.*;

/**
 * Sala real que no requiere verificación especial
 */
public class SalaReal implements ISala {
    private Sala sala;
    
    public SalaReal(Sala sala) {
        this.sala = sala;
    }
    
    @Override
    public void verificarAcceso(Cliente cliente) throws AccesoSalaVIPDenegadoException {
        // No se requiere verificación especial para salas normales
    }
    
    @Override
    public Sala getSala() {
        return sala;
    }
}
