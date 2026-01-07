/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class EntidadNoEncontradaException extends CineException {
    public EntidadNoEncontradaException(String entidad, String identificador) {
        super(entidad + " no encontrado/a con identificador: " + identificador);
    }
}
