/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class SesionNoDisponibleException extends CineException {
    public SesionNoDisponibleException(String mensaje) {
        super("La sesión no está disponible: " + mensaje);
    }
}
