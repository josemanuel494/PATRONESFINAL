/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class CancelacionFueraDePlazaException extends CineException {
    public CancelacionFueraDePlazaException() {
        super("No se puede cancelar la reserva. Debe hacerse con al menos 2 horas de antelación.");
    }
}