/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

/**
 * Interfaz para observadores de sesiones (patrón Observer)
 */
interface ObservadorSesion {
    void actualizar(Sesion sesion, String mensaje);
}