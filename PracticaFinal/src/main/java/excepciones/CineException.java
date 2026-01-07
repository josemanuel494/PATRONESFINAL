/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Excepciones personalizadas del sistema de gestión de cine
 * Según requisitos del apartado 5.3
 */

/**
 * Excepción base para el sistema
 */
public class CineException extends Exception {
    public CineException(String mensaje) {
        super(mensaje);
    }
}