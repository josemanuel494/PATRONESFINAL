/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Película en formato 3D
 */
public class Pelicula3D extends Pelicula {
    private static final long serialVersionUID = 1L;
    
    public Pelicula3D(String codigo, String titulo, String director, int anioEstreno,
                      int duracionMinutos, Genero genero, String sinopsis) {
        super(codigo, titulo, director, anioEstreno, duracionMinutos, genero, sinopsis);
    }
    
    @Override
    public double getRecargoProyeccion() {
        return 2.0; // Recargo de 2€
    }
    
    @Override
    public String getTipoProyeccion() {
        return "3D";
    }
    
    @Override
    public boolean esCompatibleConSala(Sala sala) {
        return true; // Compatible con todas las salas
    }
}
