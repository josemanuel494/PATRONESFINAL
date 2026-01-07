/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class SalaNoCompatibleIMAXException extends CineException {
    public SalaNoCompatibleIMAXException(int numeroSala) {
        super("La Sala " + numeroSala + " no es compatible con proyección IMAX. " +
              "Solo la Sala 1 soporta IMAX.");
    }
}
