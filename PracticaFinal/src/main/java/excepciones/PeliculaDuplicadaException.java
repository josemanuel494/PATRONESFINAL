/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class PeliculaDuplicadaException extends CineException {
    public PeliculaDuplicadaException(String codigo) {
        super("Ya existe una película con el código: " + codigo);
    }
}
