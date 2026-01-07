/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class ClienteDuplicadoException extends CineException {
    public ClienteDuplicadoException(String dni) {
        super("Ya existe un cliente con DNI: " + dni);
    }
}
