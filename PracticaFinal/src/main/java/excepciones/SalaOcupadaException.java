/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class SalaOcupadaException extends CineException {
    public SalaOcupadaException(String mensaje) {
        super("Sala ocupada: " + mensaje);
    }
}