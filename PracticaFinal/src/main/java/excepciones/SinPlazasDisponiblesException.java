/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class SinPlazasDisponiblesException extends CineException {
    public SinPlazasDisponiblesException(String sesion) {
        super("No hay plazas disponibles para la sesión: " + sesion);
    }
}
